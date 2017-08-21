package net.rlair.flight.test;

import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.FlightPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yang Haikun
 */

@Controller
public class TestController {
    @Autowired
    FlightPlanRepository flightRepository;

    @RequestMapping("/test")
    String test() {
        FlightPlan fp = new FlightPlan();
        flightRepository.save(fp);

        return "test";
    }
}
