package net.rlair.flight.controller;

import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yang Haikun
 */

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    FlightPlanRepository flightPlanRepository;

    @Autowired
    FlightRepository flightRepository;

    @RequestMapping("")
    String test() {
        //FlightPlan fp = new FlightPlan();
        //flightRepository.save(fp);
        Flight f = new Flight();
        flightRepository.save(f);

        return "test";
    }
}
