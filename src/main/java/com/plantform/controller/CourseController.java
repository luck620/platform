package com.plantform.controller;

import com.plantform.dto.CourseDTO;
import com.plantform.dto.CourseDetailDTO;
import com.plantform.dto.TeacherDTO;
import com.plantform.entity.*;
import com.plantform.repository.CourseRepository;
import com.plantform.repository.StudentRepository;
import com.plantform.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseRepository courseRepository;

    @Resource
    TeacherRepository teacherRepository;

    @Resource
    StudentRepository studentRepository;

    //课程详情---课程
    @ResponseBody
    @GetMapping("/findCourseDetail/{id}")
    public Course findCourseDetail(@PathVariable("id") int id){
        Course courseDetail = new Course();
        Course course = courseRepository.findCourseById(id);
        courseDetail.setId(course.getId());
        courseDetail.setStuNumber(course.getStuNumber());
        courseDetail.setName(course.getName());
        return  courseDetail;
    }

    //课程详情---教师
    @ResponseBody
    @PostMapping("/findCourseDetailTea/{id}/{pageNum}/{pageSize}")
    public Page<Teacher> findCourseDetailTea(@PathVariable("id") int id,
                                             @PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize,
                                             @RequestBody TeacherDTO teacherDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Teacher> teacherList = teacherRepository.findTeacherByCourseId(id,teacherDTO.getName(),teacherDTO.getTno(),teacherDTO.getPhone());//查询该课程的教师
        int totalElements = teacherRepository.findTeacherByCourseIdCount(id,teacherDTO.getName(),teacherDTO.getTno(),teacherDTO.getPhone());
        List<Teacher> teacherList1 = new ArrayList<>();//存放teacher的几个相关信息
        for(Teacher teacher : teacherList){
            Teacher teacher1 = new Teacher();
            teacher1.setId(teacher.getId());
            teacher1.setTno(teacher.getTno());
            teacher1.setMail(teacher.getMail());
            teacher1.setPhone(teacher.getPhone());
            teacher1.setName(teacher.getName());
            teacherList1.add(teacher1);
        }
        Page<Teacher> teacherPage = listConvertToPage1(teacherList1,totalElements,pageable);
        return teacherPage;
    }


    //课程详情---学生
    @ResponseBody
    @PostMapping("/findCourseDetailStu/{id}/{pageNum}/{pageSize}")
    public Page<Student> findCourseDetailStu(@PathVariable("id") int id,
                                             @PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize,
                                             @RequestBody Student student){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Student> studentList = studentRepository.findStudentByCourseId(id,student.getName(),student.getSno(),student.getPhone(),student.getGrade());//查询该课程的学生
        int totalElements = studentRepository.findStudentByCourseIdCount(id,student.getName(),student.getSno(),student.getPhone(),student.getGrade());
        List<Student> studentList1 = new ArrayList<>();//存放student的几个相关信息
        for(Student stu : studentList){
            Student student1 = new Student();
            student1.setId(stu.getId());
            student1.setSno(stu.getSno());
            student1.setName(stu.getName());
            student1.setPhone(stu.getPhone());
            student1.setMail(stu.getMail());
            student1.setGrade(stu.getGrade());
            studentList1.add(student1);
        }
        Page<Student> studentPage = listConvertToPage1(studentList1, totalElements, pageable);
        return studentPage;
    }

    public static <T> Page<T> listConvertToPage1(List<T> list, int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    @ResponseBody
    @GetMapping("/getCourseList/{pageNum}/{pageSize}")
    public Page<Course> getCourseList(@PathVariable("pageNum") Integer pageNum,
                                  @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseAll();
        int totalElements = courseRepository.findCourseAllCount();
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
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
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
        List<Course> courseList =  courseRepository.findAllByOthers(name,useBook,numberStart,numberEnd);
        int totalElements = courseRepository.findAllByOthersCount(name,useBook,numberStart,numberEnd);
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
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
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
