package com.plantform.controller;

import com.plantform.dto.BookDTO;
import com.plantform.entity.Book;
import com.plantform.entity.Course;
import com.plantform.entity.MyResult;
import com.plantform.repository.BookRepository;
import com.plantform.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseRepository courseRepository;

    @ResponseBody
    @GetMapping("/getCourseList/{pageNum}/{pageSize}")
    public Page<Course> getCourseList(@PathVariable("pageNum") Integer pageNum,
                                  @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return courseRepository.findAll(pageable);
    }

//    @ResponseBody
//    @PostMapping("/getCourseListByOthers/{pageNum}/{pageSize}")
//    public Page<Course> getCourseListByOthers(@PathVariable("pageNum") Integer pageNum,
//                                          @PathVariable("pageSize") Integer pageSize,
//                                          @RequestBody BookDTO bookDTO){
//        Pageable pageable = PageRequest.of(pageNum,pageSize);
//        String name = bookDTO.getName();
//        String publish = bookDTO.getPublish();
//        String type = bookDTO.getType();
//        String addTimeStart = bookDTO.getAddTimeStart();
//        String addTimeEnd = bookDTO.getAddTimeEnd();
//        String publishTimeStart = bookDTO.getPublishTimeStart();
//        String publishTimeEnd = bookDTO.getPublishTimeEnd();
//        System.out.println("name="+name+" publish="+publish+" type="+type+" addTimeStart="+addTimeStart+" addTimeEnd="+addTimeEnd+" publishTimeStart="+publishTimeStart+" publishTimeEnd="+publishTimeEnd);
//        if(name.isEmpty() || name == null){
//            name = "";
//        }
//        if(publish.isEmpty() || publish == null){
//            publish = "";
//        }
//        if(type.isEmpty() || type == null){
//            type = "";
//        }
//        System.out.println("name="+name+" publish="+publish+" type="+type);
//        return courseRepository.findAllByOthers(name,publish,type,addTimeStart,addTimeEnd,publishTimeStart,publishTimeEnd,pageable);
//    }

    @ResponseBody
    @PostMapping("/addCourse")
    public MyResult addCourse(@RequestBody BookDTO bookDTO){
        MyResult myResult = new MyResult();
        System.out.println(bookDTO.getPublishTime());
        Course course = new Course();
        course.setName(bookDTO.getName());
        Course course1 = courseRepository.save(course);
        if(course1 != null){
            myResult.setCode(200);
            myResult.setMsg("添加成功");
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findCourseById/{id}")
    public Course findCourseById(@PathVariable("id") int id){
        return courseRepository.findCourseById(id);
    }

    @ResponseBody
    @PostMapping("/editCourseById/{id}")
    public MyResult editCourseById(@PathVariable("id") int id,
                                 @RequestBody Book editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" type="+editForm.getType()+" publish="+editForm.getPublish()+" addTime="+editForm.getAddTime()+" publishTime="+editForm.getPublishTime());
//        int result = courseRepository.update(editForm.getName(),editForm.getPublish(),editForm.getType(),editForm.getAddTime().toString(),editForm.getPublishTime().toString(),id);
//        System.out.println("result= "+result);
//        MyResult myResult = new MyResult();
//        if(result == 1){
//            myResult.setCode(200);
//            myResult.setMsg("修改成功");
//            return myResult;
//        }
        return null;
    }

    @ResponseBody
    @GetMapping("/deleteCourseById/{id}")
    public MyResult deleteCourseById(@PathVariable("id") Integer id){
//        courseRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
