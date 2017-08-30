package net.rlair.flight.repository;

import net.rlair.flight.entity.Flight;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Yang Haikun
 * @version 0.1.0.0
 */
public interface FlightCustome {
    /**
     * 批量更新数据
     * @param flightList
     */
    @Transactional
    void batchUpdate(List<Flight> flightList);

    /**
     * 批量插入数据
     * @param flightList
     */
    @Transactional
    void batchInsert(List<Flight> flightList);

    /**
     * 批量删除数据
     * @param flightList
     */
    @Transactional
    void batchDelete(List<Flight> flightList);

    List<Flight> findByQuery(String sql);
}
