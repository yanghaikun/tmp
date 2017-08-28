package net.rlair.flight.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rlair.flight.common.RecordStatus;
import net.rlair.flight.common.ServiceResult;
import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.AirplaneRepository;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import net.rlair.flight.service.FlightService;
import net.rlair.flight.support.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * @author Yang Haikun
 */

@Controller
@RequestMapping("")
public class IndexController {
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    FlightPlanRepository flightPlanRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/index")
    String index(Model model) {
        //初始化数据
        if (airplaneRepository.count() == 0) {
            airplaneRepository.init();
        }
        flightService.loadFlightPlan();
        flightService.loadFlight();

        model.addAttribute("serverPath", "http://localhost:8080");

        //test
        //String sql_tmp = "CREATE TEMPORARY TABLE IF NOT EXISTS t_flight_tmp LIKE t_flight; TRUNCATE TABLE t_flight_tmp;";
        //flightPlanRepository.executeSQL(sql_tmp);

        return "index";
    }

    @RequestMapping(value = "/flightPlan/addUI")
    String flightPlanAddUI() {
        return "flightPlanEdit";
    }

    @RequestMapping(value = "/flightPlan/add")
    @ResponseBody
    ServiceResult flightPlanAdd(final FlightPlan flightPlan) {
        Log.FLIGHT.info("生成新增通知单");
        flightPlan.setStatus(RecordStatus.INSERT);
        flightPlan.setDescription("新增");
        flightPlanRepository.save(flightPlan);
        //重新加载航线计划
        flightService.loadFlightPlan();

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        result.msg = "生成通知单成功";
        return result;
    }


    @RequestMapping(value = "/flightPlan/updateUI")
    String flightPlanEditUI(@RequestParam(value = "id") final long id, final Model model) {
        FlightPlan fp = flightPlanRepository.findOne(id);
        model.addAttribute("entity", fp);

        return "flightPlanEdit";
    }

    @RequestMapping(value = "/flightPlan/update")
    @ResponseBody
    ServiceResult flightPlanUpdate(final FlightPlan flightPlan) {
        Log.FLIGHT.info("生成更新通知单 id={}", flightPlan.getId());
        FlightPlan fp = flightPlan.copy();
        fp.setStatus(RecordStatus.UPDATE);
        fp.setDescription("变更");
        flightPlanRepository.save(fp);

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        result.msg = "生成通知单成功";
        return result;
    }


    @RequestMapping(value = "/flightPlan/list")
    @ResponseBody
    ServiceResult flightPlanList(@RequestParam(value = "page", defaultValue = "1") final int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page p = flightPlanRepository.findByStatusNot(RecordStatus.FINISH, pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = flightPlanRepository.countByStatusNot(RecordStatus.FINISH);
        result.msg = "查询成功";
        return result;
    }

    /**
     * 取消航班，并不是真正的取消，是生成一个通知单
     *
     * @param flightPlan
     * @return
     */
    @RequestMapping(value = "/flightPlan/cancel")
    @ResponseBody
    ServiceResult flightPlanCancel(final FlightPlan flightPlan) {
        Log.FLIGHT.info("生成取消通知单 id={}", flightPlan.getId());
        FlightPlan fp = flightPlan.copy();
        fp.setStatus(RecordStatus.CANCEL);
        fp.setCanceled(true);
        fp.setDescription("取消");
        flightPlanRepository.save(fp);

        ServiceResult result = new ServiceResult();
        result.msg = "生成通知单成功";
        result.succeed = true;
        return result;
    }

    @RequestMapping(value = "/flightPlan/recover")
    @ResponseBody
    ServiceResult flightPlanRecover(final FlightPlan flightPlan) {
        Log.FLIGHT.info("生成恢复通知单 id={}", flightPlan.getId());
        FlightPlan fp = flightPlan.copy();
        fp.setStatus(RecordStatus.RECOVER);
        fp.setCanceled(false);
        fp.setDescription("恢复");
        flightPlanRepository.save(fp);

        ServiceResult result = new ServiceResult();
        result.msg = "生成通知单成功";
        result.succeed = true;
        return result;
    }

    @RequestMapping(value = "/flightPlan/delete")
    @ResponseBody
    ServiceResult flightPlanDelete(final FlightPlan flightPlan) {
        Log.FLIGHT.info("生成删除通知单 id={}", flightPlan.getId());
        FlightPlan fp = flightPlan.copy();
        fp.setStatus(RecordStatus.DELETE);
        fp.setDescription("删除");
        flightPlanRepository.save(fp);

        ServiceResult result = new ServiceResult();
        result.msg = "生成通知单成功";
        result.succeed = true;
        return result;
    }

    /**
     * 发布航线动态
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/flightPlan/publish")
    @ResponseBody
    ServiceResult flightPlanPublish(@RequestParam(value = "data", defaultValue = "[]") final String data) {
        Log.FLIGHT.info("发布航班动态: data={}", data);
        //TODO
        //解析json
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Long> list = mapper.readValue(data, new TypeReference<List<Long>>() {
            });
            //flightService.publishFlightPlan(list);
            flightService.publishFlightPlanSlow(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        result.msg = "发布通知单成功";
        return result;
    }


    @RequestMapping(value = "/airplane/list")
    @ResponseBody
    ServiceResult airplaneList(@RequestParam(value = "page", defaultValue = "1") final int page,
                               @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize) {
        Flight example = new Flight();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page p = airplaneRepository.findAll(pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = airplaneRepository.count();
        return result;
    }

    @RequestMapping(value = "/flight/list")
    @ResponseBody
    ServiceResult flightList(@RequestParam(value = "page", defaultValue = "1") final int page,
                             @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page p = flightRepository.findAll(pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = flightRepository.count();
        return result;
    }
}
