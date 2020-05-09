package com.plantform.controller;

import com.plantform.entity.Student;
import com.plantform.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/stu")
@CrossOrigin
public class StudentController {
    @Resource
    StudentRepository studentRepository;

    @GetMapping("/findAll/{page}/{size}")
    public Page<Student> findAll(@PathVariable("page")Integer page, @PathVariable("size")Integer size){
        PageRequest pageRequest = PageRequest.of(page,size);
        return studentRepository.findAll(pageRequest);
    }
}
