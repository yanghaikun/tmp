package net.rlair.flight.repository;

import net.rlair.flight.entity.Flight;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kevin on 2017/8/27.
 */
public class FlightRepositoryImpl implements FlightCustome {
    //获取当前线程的EntityManager实例
    @PersistenceContext
    private EntityManager em;
    private int batchSize = 10000;

    @Override
    public void batchUpdate(List<Flight> flightList) {
        if (null == flightList || flightList.isEmpty()) {
            return;
        }
        //先创建临时表
        String sql_tmp = "CREATE TEMPORARY TABLE IF NOT EXISTS t_flight_tmp LIKE t_flight;";
        getSession().createSQLQuery(sql_tmp).executeUpdate();
        sql_tmp = "TRUNCATE TABLE t_flight_tmp;";
        getSession().createSQLQuery(sql_tmp).executeUpdate();

        StringBuilder sql = new StringBuilder("INSERT INTO t_flight_tmp (`id`, `departure_time`, `arrival_time`, `airplane`, `canceled`) VALUES ");
        int count = flightList.size();
        for (int i = 0; i < count; ++i) {
            Flight f = flightList.get(i);
            sql.append(String.format("(%d, '%s', '%s', '%s', %d),", f.getId(), f.getDepartureTime(), f.getArrivalTime(), f.getAirplane(), f.isCanceled() ? 1 : 0));
            if ((i > 0 && i % batchSize == 0) || (i == count - 1)) {
                sql = sql.deleteCharAt(sql.lastIndexOf(","));
                getSession().createSQLQuery(sql.toString()).executeUpdate();
                em.flush();
                em.clear();
                sql = new StringBuilder("INSERT INTO t_flight_tmp (`id`, `departure_time`, `arrival_time`, `airplane`, `canceled`) VALUES ");
            }
        }

        sql_tmp = "UPDATE t_flight f, t_flight_tmp tmp SET f.departure_time = tmp.departure_time, f.arrival_time = tmp.arrival_time, f.airplane=tmp.airplane, f.canceled = tmp.canceled WHERE f.id = tmp.id;";
        getSession().createSQLQuery(sql_tmp).executeUpdate();
    }

    @Override
    public void batchInsert(List<Flight> flightList) {
        if (null == flightList || flightList.isEmpty()) {
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder("INSERT INTO t_flight (`date`, `day_of_week`, `departure_time`, `arrival_time`, `origin_city`, `destination_city`,  `airplane`, `flight_number`, `canceled`, `plan_id`) VALUES ");
        int count = flightList.size();
        for (int i = 0; i < count; ++i) {
            Flight f = flightList.get(i);
            sql.append(String.format("('%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', %d, '%s'),", df.format(f.getDate()), f.getDayOfWeek(), f.getDepartureTime(), f.getArrivalTime(), f.getOriginCity(), f.getDestinationCity(), f.getAirplane(), f.getFlightNumber(), f.isCanceled() ? 1 : 0, f.getPlanId()));
            if ((i > 0 && i % batchSize == 0) || (i == count - 1)) {
                sql = sql.deleteCharAt(sql.lastIndexOf(","));
                getSession().createSQLQuery(sql.toString()).executeUpdate();
                em.flush();
                em.clear();
                sql = new StringBuilder("INSERT INTO t_flight (`date`, `day_of_week`, `departure_time`, `arrival_time`, `origin_city`, `destination_city`,  `airplane`, `flight_number`, `canceled`, `plan_id`) VALUES ");
            }
        }
    }

    @Override
    public void batchDelete(List<Flight> flightList) {
        if (null == flightList || flightList.isEmpty()) {
            return;
        }
        StringBuilder sql = new StringBuilder("DELETE FROM t_flight WHERE id IN(");
        int count = flightList.size();
        for (int i = 0; i < count; ++i) {
            Flight f = flightList.get(i);
            sql.append(f.getId()).append(",");
        }
        sql = sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(");");
        getSession().createSQLQuery(sql.toString()).executeUpdate();
        em.flush();
        em.clear();
    }

    @Override
    public List<Flight> findByQuery(String sql) {
        List<Flight> result = getSession().createSQLQuery(sql).list();
        return result;
    }

    /**
     * 获取Session
     * @return
     */
    public Session getSession() {
        return (Session) em.getDelegate();
    }
}
