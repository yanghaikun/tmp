package net.rlair.flight.controller;

import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Yang Haikun
 */

@Controller
@RequestMapping("")
public class IndexController {
    @Autowired
    FlightPlanRepository flightPlanRepository;
    @Autowired
    FlightRepository flightRepository;

    @RequestMapping("/index")
    String index() {

        return "index";
    }


    @RequestMapping(value = "/flight/list")
    @ResponseBody
    List<Flight> listFlight(@RequestParam(value = "page", defaultValue = "1") final int page,
                            @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize){
        Flight example = new Flight();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page p = flightRepository.findAll(pageable);

        return p.getContent();
    }
}
