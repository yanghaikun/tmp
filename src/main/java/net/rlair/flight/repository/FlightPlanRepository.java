package net.rlair.flight.repository;

import net.rlair.flight.entity.FlightPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yang Haikun
 * @version 0.1.0.0
 */
@Repository
public interface FlightPlanRepository extends JpaRepository<FlightPlan, Long> {
}
