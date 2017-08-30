package net.rlair.flight.service;

import net.rlair.flight.common.RecordStatus;
import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import net.rlair.flight.support.log.Log;
import net.rlair.flight.support.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kevin on 2017/8/25.
 */
@Service
public class FlightService {
    @Autowired
    private FlightPlanRepository flightPlanRepository;

    @Autowired
    private FlightRepository flightRepository;

    public static Map<Long, FlightPlan> FLIGHT_PLAN = new HashMap<>();
    public static List<Flight> FLIGHT = new ArrayList<>();

    /**
     * 加载航线计划缓存
     */
    public void loadFlightPlan() {
        FLIGHT_PLAN.clear();
        List<FlightPlan> list = flightPlanRepository.findByStatusNot(RecordStatus.FINISH);
        for (FlightPlan fp : list) {
            FLIGHT_PLAN.put(fp.getId(), fp);
        }
    }


    /**
     * 加载航班动态
     */
    public void loadFlight() {
        FLIGHT.clear();
        List<Flight> list = flightRepository.findAll();
        for (Flight f : list) {
            FLIGHT.add(f);
        }
    }


    @Transactional
    public void publishFlightPlanViaMemory(List<Long> publish) {
        long begin = System.currentTimeMillis();
        //重新加载航线计划
        loadFlightPlan();
        List<FlightPlan> plans = new ArrayList<>();
        for (long id : publish) {
            FlightPlan fp = FLIGHT_PLAN.get(id);
            if (null != fp) {
                plans.add(FLIGHT_PLAN.get(id));
            }
        }

        //真正修改航班
        for (FlightPlan fp : plans) {
            //如果是新增，直接插入
            if (RecordStatus.INSERT == fp.getStatus()) {
                List<Flight> list = genFlightByPlan(fp);
                //如果航线中已经有相同的，则过滤掉该航线
                for (Iterator<Flight> iter = list.iterator(); iter.hasNext(); ) {
                    Flight f1 = iter.next();
                    for (Flight f2 : FLIGHT) {
                        if (flightEquals(f1, f2)) {
                            iter.remove();
                            break;
                        }
                    }
                }
                FLIGHT.addAll(list);
            }
            //如果是更新
            else {
                Date startDate = fp.getStartDate();
                Date endDate = fp.getEndDate();
                List<Integer> schedule = fp.getScheduleList();
                List<Date> days = Utils.getDateMachDayOfWeek(startDate, endDate, schedule);
                //遍历所有航班，找到满足条件的航班，修改信息
                for (Flight f : FLIGHT) {
                    if (!f.matchPlan(fp)) {
                        continue;
                    }
                    if (RecordStatus.DELETE == fp.getStatus()) {
                        f.setStatus(RecordStatus.DELETE);
                    } else if (RecordStatus.UPDATE == fp.getStatus() && RecordStatus.DELETE != f.getStatus()) {
                        f.copyFormPlan(fp);
                        f.setStatus(RecordStatus.UPDATE);
                    } else if (RecordStatus.CANCEL == fp.getStatus()) {
                        f.setStatus(RecordStatus.UPDATE);
                        f.setCanceled(true);
                    } else if (RecordStatus.RECOVER == fp.getStatus()) {
                        f.setStatus(RecordStatus.UPDATE);
                        f.setCanceled(false);
                    }
                }
            }
        }

        //遍历所有航线，找出修改过的航线
        List<Flight> insert = new ArrayList<>();
        List<Flight> update = new ArrayList<>();
        List<Flight> delete = new ArrayList<>();
        for (Flight f : FLIGHT) {
            if (RecordStatus.NONE == f.getStatus()) {
                continue;
            } else if (RecordStatus.UPDATE == f.getStatus()) {
                update.add(f);
            } else if (RecordStatus.INSERT == f.getStatus()) {
                insert.add(f);
            } else if (RecordStatus.DELETE == f.getStatus()) {
                delete.add(f);
            }
        }
        //根据条件增删改查
        flightRepository.batchInsert(insert);
        flightRepository.batchUpdate(update);
        flightRepository.batchDelete(delete);

        //将所有航线计划置为已生效
        for (FlightPlan fp : plans) {
            fp.setStatus(RecordStatus.FINISH);
        }
        flightPlanRepository.save(plans);
        for (Flight f : FLIGHT) {
            f.setStatus(RecordStatus.NONE);
        }

        long end = System.currentTimeMillis();
        Log.FLIGHT.info("通过内存计算方式执行完毕，共插入{}条数据，修改{}条数据，删除{}条数据，共耗时{}ms", insert.size(), update.size(), delete.size(), (end - begin));
    }

    /**
     * 直接操作数据库修改航班动态
     *
     * @param publish
     */
    @Transactional
    public void publishFlightPlanViaSQL(List<Long> publish) {
        long begin = System.currentTimeMillis();
        //重新加载航线计划
        loadFlightPlan();
        List<FlightPlan> plans = new ArrayList<>();
        for (long id : publish) {
            FlightPlan fp = FLIGHT_PLAN.get(id);
            if (null != fp) {
                plans.add(FLIGHT_PLAN.get(id));
            }
        }

        //真正修改航班
        for (FlightPlan fp : plans) {
            //如果是新增，直接插入
            if (RecordStatus.INSERT == fp.getStatus()) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                List<Flight> list = genFlightByPlan(fp);
                //先判断是否已有重复的数据
                for (Iterator<Flight> iter = list.iterator(); iter.hasNext(); ) {
                    Flight f1 = iter.next();
                    String sql = String.format("SELECT id FROM t_flight WHERE flight_number='%s' AND origin_city='%s' AND destination_city='%s' AND date='%s' AND departure_time='%s' AND arrival_time='%s'",
                            f1.getFlightNumber(), f1.getOriginCity(), f1.getDestinationCity(), df.format(f1.getDate()), f1.getDepartureTime(), f1.getArrivalTime());
                    if (!flightRepository.findByQuery(sql).isEmpty()) {
                        iter.remove();
                    }
                }
                FLIGHT.addAll(list);
            } else if (RecordStatus.UPDATE == fp.getStatus() || RecordStatus.CANCEL == fp.getStatus() || RecordStatus.RECOVER == fp.getStatus()) {
                //拼接SQL执行
                String sql = fp.SQLUpdate();
                flightPlanRepository.executeSQL(sql);
                flightRepository.flush();
            } else if (RecordStatus.DELETE == fp.getStatus()) {
                //拼接SQL执行
                String sql = fp.SQLDelete();
                flightPlanRepository.executeSQL(sql);
                flightRepository.flush();
            }

            //修改fp
            fp.setStatus(RecordStatus.FINISH);
            flightPlanRepository.save(fp);
        }

        long end = System.currentTimeMillis();
        Log.FLIGHT.info("通过SQL方式执行完毕，共耗时{}ms", (end - begin));
    }

    public List<Flight> genFlightByPlan(FlightPlan fp) {
        List<Flight> result = new ArrayList<>();
        Date startDate = fp.getStartDate();
        Date endDate = fp.getEndDate();
        List<Integer> schedule = fp.getScheduleList();
        //找出所有满足条件的日期
        List<Date> days = Utils.getDateMachDayOfWeek(startDate, endDate, schedule);
        //插入新航班
        for (Date d : days) {
            Flight flight = new Flight();
            flight.setDayOfWeek(Utils.getDayOfWeek(d));
            flight.setStatus(RecordStatus.INSERT);
            flight.setDate(d);
            flight.copyFormPlan(fp);

            result.add(flight);
        }

        return result;
    }

    /**
     * 判断两个航班是否相同
     *
     * @param f1
     * @param f2
     * @return
     */
    public boolean flightEquals(Flight f1, Flight f2) {
        //判断航班号
        if (!(f1.getFlightNumber().equals(f2.getFlightNumber()))) {
            return false;
        }
        //判断出发城市
        if (!(f1.getOriginCity().equals(f2.getOriginCity()))) {
            return false;
        }
        //判断到达城市
        if (!(f1.getDestinationCity().equals(f2.getDestinationCity()))) {
            return false;
        }
        //判断日期
        if (!(f1.getDate().getTime() == f2.getDate().getTime())) {
            return false;
        }
        //判断出发时刻
        if (!(f1.getDepartureTime().equals(f2.getDepartureTime()))) {
            return false;
        }
        //判断到达时刻
        if (!(f1.getArrivalTime().equals(f2.getArrivalTime()))) {
            return false;
        }

        return true;
    }
}
