package net.rlair.flight.repository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Yang Haikun
 * @version 0.1.0.0
 */
public interface FlightPlanCustome {
    @Transactional
    void executeSQL(String sql);

    EntityManager getEntityManager();
}
