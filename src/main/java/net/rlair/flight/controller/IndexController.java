package net.rlair.flight.controller;

import net.rlair.flight.common.ServiceResult;
import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.AirplaneRepository;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Yang Haikun
 */

@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    FlightPlanRepository flightPlanRepository;
    @Autowired
    FlightRepository flightRepository;

    @RequestMapping(value = "")
    String index() {
        //初始化数据
        if(airplaneRepository.count() == 0) {
            airplaneRepository.init();
        }

        return "index";
    }


    @RequestMapping(value = "/flightPlan/add")
    @ResponseBody
    ServiceResult addFlightPlan(final FlightPlan flightPlan){
        flightPlanRepository.save(flightPlan);

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        return result;
    }


    @RequestMapping(value = "/flight/list")
    @ResponseBody
    ServiceResult listFlight(@RequestParam(value = "page", defaultValue = "1") final int page,
                             @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize){
        Flight example = new Flight();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page p = flightRepository.findAll(pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = flightPlanRepository.count();
        return result;
    }

    @RequestMapping(value = "/airplane/list")
    @ResponseBody
    ServiceResult listAirplane(@RequestParam(value = "page", defaultValue = "1") final int page,
                             @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize){
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
}
