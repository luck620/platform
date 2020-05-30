package com.plantform.controller;

import com.plantform.entity.MyResult;
import com.plantform.repository.PeriodRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/period")
public class PeriodController {
    @Resource
    PeriodRepository periodRepository;

    @ResponseBody
    @GetMapping("/addVideoResource/{videoURL}/{id}")
    public MyResult addVideoResource(@PathVariable("videoURL")String videoURL,
                                     @PathVariable("id")int id){
        String videoUrl = "http://qaath1lbd.bkt.clouddn.com/"+videoURL;
        MyResult myResult = new MyResult();
        int result = periodRepository.updatePeriod(videoUrl,id);
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("完善成功");
            return myResult;
        }
        return myResult;
    }
}
