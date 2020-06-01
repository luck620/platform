package com.plantform.controller;

import com.plantform.dto.ExamDTO;
import com.plantform.entity.Exam;
import com.plantform.entity.MyResult;
import com.plantform.repository.ExamRepository;
import javafx.scene.Parent;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Resource
    ExamRepository examRepository;

    @ResponseBody
    @GetMapping("/getExam/{courseId}")
    public List<ExamDTO> getExam(@PathVariable("courseId")int courseId){
        List<ExamDTO> examDTOList = new ArrayList<>();
        List<Exam> examList = examRepository.getExam(courseId);
        if(examList.size() > 0){
            for(Exam exam: examList){
                ExamDTO examDTO = new ExamDTO();
                examDTO.setId(exam.getId());
                examDTO.setType(exam.getType());
                examDTO.setCourseId(exam.getCourse().getId());
                examDTO.setTitle(exam.getTitle());
                examDTO.setChooseA(exam.getChooseA());
                examDTO.setChooseB(exam.getChooseB());
                examDTO.setChooseC(exam.getChooseC());
                examDTO.setChooseD(exam.getChooseD());
                examDTO.setContent(exam.getContent());
                examDTO.setAnswer(exam.getAnswer());
                examDTOList.add(examDTO);
            }
        }
        return examDTOList;
    }

    //上传题目信息
    @ResponseBody
    @PostMapping("/updateExamInfo/{id}")
    public MyResult updateExamInfo(@PathVariable("id")int id,
                                   @RequestBody ExamDTO examDTO){
        System.out.println("title="+examDTO.getTitle()+" A="+examDTO.getChooseA()+" B="+examDTO.getChooseB()+" C="+examDTO.getChooseC() +" D="+examDTO.getChooseD()+" answer="+examDTO.getAnswer());
        int result = examRepository.updateExamInfo(examDTO.getTitle(),examDTO.getChooseA(),examDTO.getChooseB(),examDTO.getChooseC(),examDTO.getChooseD(),examDTO.getAnswer(),examDTO.getContent(),id);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("创建成功");
        }
        return myResult;
    }

    //上传题目信息
    @ResponseBody
    @PostMapping("/submitAnswer/{id}/{courseId}")
    public MyResult submitAnswer(@PathVariable("id")int id,
                                   @PathVariable("courseId") int courseId,
                                   @RequestBody Map<Integer,String> map){
        int score = 0;
        if(map.size() > 0) {
            for (int key : map.keySet()) {
                System.out.println("key= " + key + " and value= " + map.get(key));
                String correctAnswer = examRepository.getCorrectAnswer(key);
                if(correctAnswer.equals(map.get(key))){
                    score ++;
                }
            }
            System.out.println(score);
            int result = examRepository.addScore(score,id,courseId);
            System.out.println("result="+result);
        }
        MyResult myResult = new MyResult();
//        if(result == 1){
        myResult.setCode(200);
        myResult.setMsg("创建成功");
//        }
        return myResult;
    }

    //上传题目信息
    @ResponseBody
    @GetMapping("/hasScore/{id}/{courseId}")
    public String hasScore(@PathVariable("id")int id,
                             @PathVariable("courseId")int courseId){
        String result = examRepository.hasScore(id,courseId);
        System.out.println(result);
        return result;
    }
}
