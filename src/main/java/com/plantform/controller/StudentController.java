package com.plantform.controller;

import com.plantform.dto.TeacherDTO;
import com.plantform.entity.Course;
import com.plantform.entity.MyResult;
import com.plantform.entity.Student;
import com.plantform.entity.Teacher;
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
@RequestMapping("/student")
@CrossOrigin
public class StudentController {
    @Resource
    StudentRepository studentRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list,int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    @ResponseBody
    @GetMapping("/getStudentList/{pageNum}/{pageSize}")
    public Page<Student> getTeacherList(@PathVariable("pageNum") Integer pageNum,
                                           @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Student> studentList = studentRepository.findStudentAll();
        int totalElements = studentRepository.findStudentAllCount();
        List<Student> studentList1 = new ArrayList<>();
        if(studentList!=null && !studentList.isEmpty()) {
            for (Student student : studentList) {
                Student student1 = new Student();
                student1.setId(student.getId());
                student1.setName(student.getName());
                student1.setGrade(student.getGrade());
                student1.setMail(student.getMail());
                student1.setPassword(student.getPassword());
                student1.setPhone(student.getPhone());
                student1.setSno(student.getSno());
                studentList1.add(student1);
            }
        }
        Page<Student> studentPage = listConvertToPage1(studentList1, totalElements, pageable);
        return studentPage;
    }

    @ResponseBody
    @PostMapping("/getStudentListByOthers/{pageNum}/{pageSize}")
    public Page<Student> getTeacherListByOthers(@PathVariable("pageNum") Integer pageNum,
                                                   @PathVariable("pageSize") Integer pageSize,
                                                   @RequestBody Student student){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        System.out.println("name="+student.getName()+" sno="+student.getSno()+" phone="+student.getPhone()+" mail="+student.getMail()+" grade="+student.getGrade());
        List<Student> studentList = studentRepository.findAllByOthers(student.getName(),student.getSno(),student.getPhone(),student.getMail(),student.getGrade());
        int totalElements = studentRepository.findAllByOthersCount(student.getName(),student.getSno(),student.getPhone(),student.getMail(),student.getGrade());
        List<Student> studentList1 = new ArrayList<>();
        if(studentList!=null && !studentList.isEmpty()) {
            for (Student stu : studentList) {
                Student student1 = new Student();
                student1.setId(stu.getId());
                student1.setName(stu.getName());
                student1.setGrade(stu.getGrade());
                student1.setMail(stu.getMail());
                student1.setPassword(stu.getPassword());
                student1.setPhone(stu.getPhone());
                student1.setSno(stu.getSno());
                studentList1.add(student1);
            }
        }
        Page<Student> studentPage = listConvertToPage1(studentList1, totalElements, pageable);
        return studentPage;
    }

    @ResponseBody
    @PostMapping("/addStudent")
    public MyResult addStudent(@RequestBody Student student){
        MyResult myResult = new MyResult();
        Student student1 = new Student();
        student1.setName(student.getName());
        student1.setPhone(student.getPhone());
        student1.setSno(student.getSno());
        student1.setMail(student.getMail());
        student1.setPassword(student.getPassword());
        student1.setGrade(student.getGrade());
        Student student2 = studentRepository.save(student1);
        if(student2 != null){
            myResult.setCode(200);
            myResult.setMsg("添加成功");
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findStudentById/{id}")
    public Student findStudentById(@PathVariable("id") int id){
        Student student = studentRepository.findStudentById(id);
        Student student1 = new Student();
        student1.setId(student.getId());
        student1.setName(student.getName());
        student1.setPhone(student.getPhone());
        student1.setSno(student.getSno());
        student1.setMail(student.getMail());
        student1.setPassword(student.getPassword());
        student1.setGrade(student.getGrade());
        return student1;
    }

    @ResponseBody
    @PostMapping("/editStudentById/{id}")
    public MyResult editStudentById(@PathVariable("id") int id,
                                    @RequestBody Student editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" phone="+editForm.getPhone()+" getSno="+editForm.getSno()+" getMail="+editForm.getMail()+" getPassword="+editForm.getPassword()+" Grade="+editForm.getGrade());
        int result = studentRepository.update(editForm.getName(),editForm.getSno(),editForm.getPhone(),editForm.getMail(),editForm.getGrade(),editForm.getPassword(),id);
        System.out.println("result= "+result);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("修改成功");
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/deleteStudentById/{id}")
    public MyResult deleteStudentById(@PathVariable("id") int id){
        studentRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
