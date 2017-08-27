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
    @Transactional
    void batchUpdate(List<Flight> flightList);
    @Transactional
    void batchInsert(List<Flight> flightList);
}
