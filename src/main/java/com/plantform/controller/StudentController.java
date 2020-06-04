package com.plantform.controller;

import com.plantform.dto.RegisterDTO;
import com.plantform.dto.StudentDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {
    @Resource
    StudentRepository studentRepository;

    @Resource
    TeacherRepository teacherRepository;

    //登陆
    @ResponseBody
    @PostMapping("/login")
    public MyResult login(@RequestBody Student student){
        MyResult myResult = new MyResult();
        System.out.println(student.getPhone()+" "+student.getPassword());
        Student student1 =  studentRepository.getStudentBy(student.getPhone(),student.getPassword());
        Teacher teacher = teacherRepository.getTeacherBy(student.getPhone(),student.getPassword());
        Map<String,Object> m = new HashMap<String,Object>();
        //判断是学生还是教师用户登录，学生用户登陆返回学生类型至前端，教师则返回教师类型
        if(student1 != null && teacher == null) {
            m.put("studentId",student1.getId());
            String token = JavaWebToken.createJavaWebToken(m);                // 根据存在用户的id生成token字符串
            myResult.setCode(200);
            myResult.setMsg("登陆成功");
            myResult.setToken(token);
            myResult.setLoginType("student");
            myResult.setLoginId(student1.getId());
        }
        if(teacher != null && student1 == null){
            m.put("teacher",teacher.getId());
            String token = JavaWebToken.createJavaWebToken(m);                // 根据存在用户的id生成token字符串
            myResult.setCode(200);
            myResult.setMsg("登陆成功");
            myResult.setToken(token);
            myResult.setLoginType("teacher");
            myResult.setLoginId(teacher.getId());
        }
        return myResult;
    }

    //更换头像
    @ResponseBody
    @GetMapping("/changeHeadById/{id}/{imageURL}")
    public MyResult changeHeadById(@PathVariable("id") int id,
                                   @PathVariable("imageURL") String imageURL){
        System.out.println("id="+id+" imageURL="+imageURL);
        String imageUrl = "http://qaath1lbd.bkt.clouddn.com/" + imageURL;
        int result = studentRepository.changeHead(imageUrl,id);
        System.out.println("result= "+result);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("完善成功");
            return myResult;
        }
        return myResult;
    }

    //注册
    @ResponseBody
    @PostMapping("/register")
    public MyResult register(@RequestBody RegisterDTO registerDTO){
        MyResult myResult = new MyResult();
        System.out.println(registerDTO.getPhone()+" "+registerDTO.getPassword()+" "+registerDTO.getType());
        //如果是学生注册的话
        if(registerDTO.getType().equals("student")){
            Student student = new Student();
            student.setPhone(registerDTO.getPhone());
            student.setPassword(registerDTO.getPassword());
            Student student1 = studentRepository.save(student);
            if(student1 != null){
                myResult.setCode(200);
                myResult.setMsg("注册成功");
            }
        }else if(registerDTO.getType().equals("teacher")){
            Teacher teacher = new Teacher();
            teacher.setPhone(registerDTO.getPhone());
            teacher.setPassword(registerDTO.getPassword());
            Teacher teacher1 = teacherRepository.save(teacher);
            if(teacher1 != null){
                myResult.setCode(200);
                myResult.setMsg("注册成功");
            }
        }
        return myResult;
    }

    //查验个人信息是否完善
    @ResponseBody
    @GetMapping("/checkInfo/{type}/{id}")
    public Boolean checkInfo(@PathVariable("type")String type , @PathVariable("id") int id){
        Boolean b = true;
        if(type.equals("student")){
            Student student = studentRepository.checkInfo(id);
            if(student.getGrade() == null || student.getMail() == null || student.getSno() == null || student.getName() == null){
                b = false;
            }
        }
        if(type.equals("teacher")){
            Teacher teacher = teacherRepository.checkInfo(id);
            if(teacher.getMail() == null || teacher.getTno() == null || teacher.getName() == null){
                b = false;
            }
        }
        return b;
    }

    public static <T> Page<T> listConvertToPage1(List<T> list,int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    @ResponseBody
    @GetMapping("/getStudentList/{pageNum}/{pageSize}")
    public Page<Student> getStudentList(@PathVariable("pageNum") Integer pageNum,
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
                student1.setImageUrl(student.getImageUrl());
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
                student1.setImageUrl(stu.getImageUrl());
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
        student1.setImageUrl("http://qaath1lbd.bkt.clouddn.com/"+student.getImageUrl());
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
        student1.setImageUrl(student.getImageUrl());
        return student1;
    }


    //完善信息
    @ResponseBody
    @PostMapping("/consummateStudentById/{id}")
    public MyResult consummateStudentById(@PathVariable("id") int id,
                                          @RequestBody StudentDTO editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" sno="+editForm.getSno()+" getMail="+editForm.getMail());
        int result = studentRepository.consummate(editForm.getName(),editForm.getSno(),editForm.getMail(),id);
        System.out.println("result= "+result);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("完善成功");
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @PostMapping("/editStudentById/{id}")
    public MyResult editStudentById(@PathVariable("id") int id,
                                    @RequestBody Student editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" phone="+editForm.getPhone()+" getSno="+editForm.getSno()+" getMail="+editForm.getMail()+" getPassword="+editForm.getPassword()+" Grade="+editForm.getGrade()+" imageUrl="+editForm.getImageUrl());
        int result = studentRepository.update(editForm.getName(),editForm.getSno(),editForm.getPhone(),editForm.getMail(),editForm.getGrade(),editForm.getPassword(),"http://qaath1lbd.bkt.clouddn.com/"+editForm.getImageUrl(),id);
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
