package net.rlair.flight.controller;

import net.rlair.flight.common.ServiceResult;
import net.rlair.flight.entity.Flight;
import net.rlair.flight.entity.FlightPlan;
import net.rlair.flight.repository.AirplaneRepository;
import net.rlair.flight.repository.FlightPlanRepository;
import net.rlair.flight.repository.FlightRepository;
import net.rlair.flight.support.log.Log;
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

/**
 * @author Yang Haikun
 */

@Controller
@RequestMapping("")
public class IndexController {
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    FlightPlanRepository flightPlanRepository;
    @Autowired
    FlightRepository flightRepository;

    @RequestMapping(value = "/index")
    String index(Model model) {
        //初始化数据
        if(airplaneRepository.count() == 0) {
            airplaneRepository.init();
        }

        model.addAttribute("serverPath", "http://localhost:8080");

        return "index";
    }

    @RequestMapping(value = "/flightPlan/editUI")
    String flightPlanEditUI(){
        return "flightPlanEdit";
    }


    @RequestMapping(value = "/flightPlan/save")
    @ResponseBody
    ServiceResult flightPlanSave(final FlightPlan flightPlan){
        flightPlanRepository.save(flightPlan);

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        result.msg = "保存航线成功";
        return result;
    }

    @RequestMapping(value = "/flightPlan/list")
    @ResponseBody
    ServiceResult flightPlanList(@RequestParam(value = "page", defaultValue = "1") final int page,
                              @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize){
        FlightPlan example = new FlightPlan();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page p = flightPlanRepository.findAll(pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = flightPlanRepository.count();
        result.msg = "查询成功";
        return result;
    }

    @RequestMapping(value = "/flightPlan/publish")
    @ResponseBody
    ServiceResult flightPlanPublish(@RequestParam(value = "data", defaultValue = "[]") final String data){

        Log.FLIGHT.info("发布航班动态: data={}", data);

        ServiceResult result = new ServiceResult();
        result.succeed = true;
        result.msg = "查询成功";
        return result;
    }


    @RequestMapping(value = "/flight/list")
    @ResponseBody
    ServiceResult flightList(@RequestParam(value = "page", defaultValue = "1") final int page,
                          @RequestParam(value = "pageSize", defaultValue = "10") final int pageSize){
        Flight example = new Flight();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page p = flightRepository.findAll(pageable);

        ServiceResult result = new ServiceResult();
        result.data = p.getContent();
        result.succeed = true;
        result.totalRow = flightRepository.count();
        return result;
    }

    @RequestMapping(value = "/airplane/list")
    @ResponseBody
    ServiceResult airplaneList(@RequestParam(value = "page", defaultValue = "1") final int page,
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
