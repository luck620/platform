package com.plantform.controller;

import com.plantform.dto.TeacherDTO;
import com.plantform.entity.Course;
import com.plantform.entity.MyResult;
import com.plantform.entity.Teacher;
import com.plantform.repository.CourseRepository;
import com.plantform.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    TeacherRepository teacherRepository;

    @Resource
    CourseRepository courseRepository;

    @ResponseBody
    @GetMapping("/findAllType")
    public List<String> findAllType(){
        return teacherRepository.findAllType();
    }

    public static <T> Page<T> listConvertToPage1(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    @ResponseBody
    @GetMapping("/getTeacherList/{pageNum}/{pageSize}")
    public Page<TeacherDTO> getTeacherList(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Teacher> teacherList = teacherRepository.findTeacherAll(pageable);
        List<TeacherDTO> teacherDTOList = new ArrayList<>();
        if(teacherList!=null && !teacherList.isEmpty()) {
            for (Teacher teacher : teacherList) {
                TeacherDTO teacherDTO = new TeacherDTO();
                teacherDTO.setId(teacher.getId());
                teacherDTO.setName(teacher.getName());
                teacherDTO.setPhone(teacher.getPhone());
                teacherDTO.setTno(teacher.getTno());
                teacherDTO.setMail(teacher.getMail());
                teacherDTO.setPassword(teacher.getPassword());
                if(teacher.getCourse() != null) {
                    teacherDTO.setCourseId(teacher.getCourse().getId());
                    teacherDTO.setCourseName(teacher.getCourse().getName());
                }else{
                    teacherDTO.setCourseName("");
                }
                teacherDTOList.add(teacherDTO);
            }
        }
        Page<TeacherDTO> teacherDTOPage = listConvertToPage1(teacherDTOList, pageable);
        return teacherDTOPage;

    }

    @ResponseBody
    @PostMapping("/getTeacherListByOthers/{pageNum}/{pageSize}")
    public Page<TeacherDTO> getTeacherListByOthers(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestBody TeacherDTO teacherDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        String name = teacherDTO.getName();
        String phone = teacherDTO.getPhone();
        String tno = teacherDTO.getTno();
        String mail = teacherDTO.getMail();
        String courseName = teacherDTO.getCourseName();
        System.out.println("name="+name+" phone="+phone+" tno="+tno+" mail="+mail+" courseName="+courseName);
        List<Teacher> teacherList = teacherRepository.findAllByOthers(name,phone,tno,mail,courseName,pageable);
        List<TeacherDTO> teacherDTOList = new ArrayList<>();
        if(teacherList!=null && !teacherList.isEmpty()) {
            for (Teacher teacher : teacherList) {
                TeacherDTO teacherDTO1 = new TeacherDTO();
                teacherDTO1.setId(teacher.getId());
                teacherDTO1.setName(teacher.getName());
                teacherDTO1.setPhone(teacher.getPhone());
                teacherDTO1.setTno(teacher.getTno());
                teacherDTO1.setMail(teacher.getMail());
                teacherDTO1.setPassword(teacher.getPassword());
                if(teacher.getCourse() != null) {
                    teacherDTO1.setCourseId(teacher.getCourse().getId());
                    teacherDTO1.setCourseName(teacher.getCourse().getName());
                }else{
                    teacherDTO1.setCourseName("");
                }
                teacherDTOList.add(teacherDTO1);
            }
        }
        Page<TeacherDTO> teacherDTOPage = listConvertToPage1(teacherDTOList, pageable);
        return teacherDTOPage;
    }

    @ResponseBody
    @PostMapping("/addTeacher")
    public MyResult addTeacher(@RequestBody TeacherDTO teacherDTO){
        MyResult myResult = new MyResult();
        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.getName());
        teacher.setPhone(teacherDTO.getPhone());
        teacher.setTno(teacherDTO.getTno());
        teacher.setMail(teacherDTO.getMail());
        teacher.setPassword(teacherDTO.getPassword());
        String courseName = teacherDTO.getCourseName();
        Course course = courseRepository.findCourseByName(courseName);
        teacher.setCourse(course);
        Teacher teacher1 = teacherRepository.save(teacher);
        if(teacher1 != null){
            myResult.setCode(200);
            myResult.setMsg("添加成功");
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findTeacherById/{id}")
    public TeacherDTO findTeacherById(@PathVariable("id") int id){
        Teacher teacher =  teacherRepository.findTeacherById(id);
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setName(teacher.getName());
        teacherDTO.setPhone(teacher.getPhone());
        teacherDTO.setTno(teacher.getTno());
        teacherDTO.setMail(teacher.getMail());
        teacherDTO.setPassword(teacher.getPassword());
        if(teacher.getCourse() != null) {
            teacherDTO.setCourseId(teacher.getCourse().getId());
            teacherDTO.setCourseName(teacher.getCourse().getName());
        }else{
            teacherDTO.setCourseName("");
        }
        return teacherDTO;
    }

    @ResponseBody
    @PostMapping("/editTeacherById/{id}")
    public MyResult editTeacherById(@PathVariable("id") int id,
                                    @RequestBody TeacherDTO editForm){
        String courseName = editForm.getCourseName();
        Course course = courseRepository.findCourseByName(courseName);
        System.out.println("id="+id+" name="+editForm.getName()+" phone="+editForm.getPhone()+" getTno="+editForm.getTno()+" getMail="+editForm.getMail()+" getPassword="+editForm.getPassword()+" course="+course.toString());
        int result = teacherRepository.update(editForm.getName(),editForm.getPhone(),editForm.getTno(),editForm.getMail(),editForm.getPassword(),course,id);
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
    @GetMapping("/deleteTeacherById/{id}")
    public MyResult deleteTeacherById(@PathVariable("id") int id){
        teacherRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
