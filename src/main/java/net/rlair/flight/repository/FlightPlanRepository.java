package net.rlair.flight.repository;

import net.rlair.flight.common.RecordStatus;
import net.rlair.flight.entity.FlightPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yang Haikun
 * @version 0.1.0.0
 */
public interface FlightPlanRepository extends JpaRepository<FlightPlan, Long>, FlightPlanCustome {
    Page<FlightPlan> findByStatusNot(RecordStatus status, Pageable pageable);
    List<FlightPlan> findByStatusNot(RecordStatus status);
    int countByStatusNot(RecordStatus status);
}
