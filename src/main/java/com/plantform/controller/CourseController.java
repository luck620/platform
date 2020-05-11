package com.plantform.controller;

import com.plantform.dto.BookDTO;
import com.plantform.dto.CourseDTO;
import com.plantform.entity.Book;
import com.plantform.entity.Course;
import com.plantform.entity.MyResult;
import com.plantform.repository.BookRepository;
import com.plantform.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseRepository courseRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    @ResponseBody
    @GetMapping("/getCourseList/{pageNum}/{pageSize}")
    public Page<Course> getCourseList(@PathVariable("pageNum") Integer pageNum,
                                  @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseAll(pageable);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setStuNumber(course.getStuNumber());
                course1.setName(course.getName());
                course1.setUseBook(course.getUseBook());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1,pageable);
        return coursePage;
    }

    @ResponseBody
    @PostMapping("/getCourseListByOthers/{pageNum}/{pageSize}")
    public Page<Course> getCourseListByOthers(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestBody CourseDTO courseDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        String name = courseDTO.getName();
        String useBook = courseDTO.getUseBook();
        int numberStart = courseDTO.getNumberStart();
        int numberEnd = courseDTO.getNumberEnd();
        System.out.println("name="+name+" useBook="+useBook+" numberStart="+numberStart+" numberEnd="+numberEnd);
        List<Course> courseList =  courseRepository.findAllByOthers(name,useBook,numberStart,numberEnd,pageable);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setStuNumber(course.getStuNumber());
                course1.setName(course.getName());
                course1.setUseBook(course.getUseBook());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1,pageable);
        return coursePage;
    }

    @ResponseBody
    @PostMapping("/addCourse")
    public MyResult addCourse(@RequestBody Course course){
        MyResult myResult = new MyResult();
        course.setStuNumber(0);
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
        Course course =  courseRepository.findCourseById(id);
        Course course1 = new Course();
        course1.setId(course.getId());
        course1.setName(course.getName());
        course1.setUseBook(course.getUseBook());
        course1.setStuNumber(course.getStuNumber());
        return course1;
    }

    @ResponseBody
    @PostMapping("/editCourseById/{id}")
    public MyResult editCourseById(@PathVariable("id") int id,
                                 @RequestBody CourseDTO editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" useBook="+editForm.getUseBook());
        int result = courseRepository.update(editForm.getName(),editForm.getUseBook(),id);
        System.out.println("result= "+result);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("修改成功");
            return myResult;
        }
        return null;
    }

    @ResponseBody
    @GetMapping("/deleteCourseById/{id}")
    public MyResult deleteCourseById(@PathVariable("id") Integer id){
        courseRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
