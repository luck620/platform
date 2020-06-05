package com.plantform.controller;

import com.plantform.dto.PeriodDTO;
import com.plantform.dto.PeriodSTDTO;
import com.plantform.entity.MyResult;
import com.plantform.entity.Period;
import com.plantform.repository.PeriodRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    //查询课程课时，以便上传视频
    @ResponseBody
    @GetMapping("/getInfo/{id}")
    public List<PeriodDTO> getInfo(@PathVariable("id")int id){
        System.out.println("id="+id);
        List<String> weekList = periodRepository.getWeekSTById(id);
        List<PeriodDTO> periodDTOList = new ArrayList<>();
        if(weekList.size() > 0 ){
            for(String weekST : weekList){
                PeriodDTO periodDTO = new PeriodDTO();
                List<PeriodSTDTO> periodSTDTOList = new ArrayList<>();
                List<Period> periodList = periodRepository.getPeriodSTById(weekST,id);
                if(periodList.size() > 0 ){
                    for(Period period : periodList){
                        PeriodSTDTO periodSTDTO = new PeriodSTDTO();
                        periodSTDTO.setId(period.getId());
                        periodSTDTO.setPeriodST(period.getPeriodST());
                        periodSTDTO.setVideoUrl(period.getVideoUrl());
                        periodSTDTOList.add(periodSTDTO);
                    }
                }
                periodDTO.setWeekST(weekST);
                periodDTO.setPeriodList(periodSTDTOList);
                periodDTOList.add(periodDTO);
            }
        }
        return periodDTOList;
    }


    @ResponseBody
    @GetMapping("/getPeriodById/{id}")
    public PeriodSTDTO getPeriodById(@PathVariable("id")int id){
        System.out.println("id="+id);
        Period period = periodRepository.getPeriodById(id);
        PeriodSTDTO periodSTDTO = new PeriodSTDTO();
        periodSTDTO.setId(period.getId());
        periodSTDTO.setPeriodST(period.getPeriodST());
        periodSTDTO.setVideoUrl(period.getVideoUrl());
        return periodSTDTO;
    }
}
