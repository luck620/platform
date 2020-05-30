package com.plantform.controller;

import com.plantform.dto.*;
import com.plantform.entity.*;
import com.plantform.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    CourseRepository courseRepository;

    @Resource
    TeacherRepository teacherRepository;

    @Resource
    StudentRepository studentRepository;

    @Resource
    PeriodRepository periodRepository;

    //教师添加课程
    @ResponseBody
    @PostMapping("/addCourseByTeacher/{id}/{imageURL}")
    public MyResult addCourseByTeacher(@PathVariable("id") int id,
                                     @PathVariable("imageURL")String imageURL,
                                     @RequestBody CourseDTO courseDTO){
        System.out.println(courseDTO.getCourseNO()+courseDTO.getName()+courseDTO.getPeriodNum()+courseDTO.getWeekNum()+courseDTO.getDescription());
        String imageUrl = "http://qaath1lbd.bkt.clouddn.com/" + imageURL;
        int result = courseRepository.addCourseByTeacher(courseDTO.getCourseNO(),courseDTO.getName(),courseDTO.getDescription(),imageUrl,courseDTO.getWeekNum(),courseDTO.getPeriodNum(),id);
        List<Integer> courseId = courseRepository.findByCourseNO(courseDTO.getCourseNO());
        System.out.println("courseId="+courseId.get(0));
        for(int i=1;i<=courseDTO.getWeekNum();i++){
            for(int j=1;j<=courseDTO.getPeriodNum();j++){
                periodRepository.addPeriod("第"+j+"课时","第"+i+"周",courseId.get(0));
            }
        }
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("创建成功");
            return myResult;
        }
        return null;
    }

    //查询课程课时，以便上传视频
    @ResponseBody
    @GetMapping("/getInfo/{courseNO}")
    public List<PeriodDTO> getInfo(@PathVariable("courseNO")String courseNO){
        System.out.println("courseNO="+courseNO);
        List<String> weekList = periodRepository.getWeekST(courseNO);
        List<PeriodDTO> periodDTOList = new ArrayList<>();
        if(weekList.size() > 0 ){
            for(String weekST : weekList){
                PeriodDTO periodDTO = new PeriodDTO();
                List<PeriodSTDTO> periodSTDTOList = new ArrayList<>();
                List<Period> periodList = periodRepository.getPeriodST(weekST,courseNO);
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

    //课程详情---课程
    @ResponseBody
    @GetMapping("/findCourseDetail/{id}")
    public Course findCourseDetail(@PathVariable("id") int id){
        Course courseDetail = new Course();
        Course course = courseRepository.findCourseById(id);
        courseDetail.setId(course.getId());
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
    @GetMapping("/getCourseListWithStuId/{pageNum}/{pageSize}/{id}")
    public Page<Course> getCourseListWithStuId(@PathVariable("pageNum") Integer pageNum,
                                                 @PathVariable("pageSize") Integer pageSize,
                                                 @PathVariable("id")int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseListWithStuId(id);
        int totalElements = courseRepository.findCourseListWithStuIdCount(id);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    @ResponseBody
    @GetMapping("/getCourseListByTeacherId/{pageNum}/{pageSize}/{id}")
    public Page<Course> getCourseListByTeacherId(@PathVariable("pageNum") Integer pageNum,
                                                 @PathVariable("pageSize") Integer pageSize,
                                                 @PathVariable("id")int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseAllByTeacherId(id);
        int totalElements = courseRepository.findCourseAllByTeacherIdCount(id);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
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
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    @ResponseBody
    @GetMapping("/getCourseListWithoutPage")
    public List<Course> getCourseList(){
        List<Course> courseList =  courseRepository.findAll();
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        return courseList1;
    }

    //学生获取作业资源
    @ResponseBody
    @GetMapping("/getCourseTestByStuId/{pageNum}/{pageSize}/{id}")
    public Page<Course> getCourseTestByStuId(@PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("id") int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseStuTest(id);
        int totalElements = courseRepository.findCourseTestStuCount(id);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    //教师获取作业资源
    @ResponseBody
    @GetMapping("/getCourseTestByTeaId/{pageNum}/{pageSize}/{id}")
    public Page<Course> getCourseTestByTeaId(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("id") int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseTest(id);
        int totalElements = courseRepository.findCourseTestCount(id);
        List<Course> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                Course course1 = new Course();
                course1.setId(course.getId());
                course1.setName(course.getName());
                course1.setImageUrl(course.getImageUrl());
                course1.setCourseNO(course.getCourseNO());
                course1.setDescription(course.getDescription());
                course1.setPeriodNum(course.getPeriodNum());
                course1.setWeekNum(course.getWeekNum());
                course1.setTestUrl(course.getTestUrl());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    //发布作业
    @ResponseBody
    @GetMapping("/addCourseTest/{id}/{wordURL}")
    public MyResult addCourseTest(@PathVariable("id")int id,
                                  @PathVariable("wordURL")String wordURL){
        String wordUrl = "http://qaath1lbd.bkt.clouddn.com/"+wordURL;
        MyResult myResult = new MyResult();
        int result = courseRepository.updateCourseTest(wordUrl,id);
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("发布作业成功");
            return myResult;
        }
        return myResult;
    }

    //学生选择课程
    @ResponseBody
    @GetMapping("/chooseCourse/{id}/{courseId}")
    public MyResult chooseCourse(@PathVariable("id")int id,
                                  @PathVariable("courseId")int courseId){
        Student student = studentRepository.findStudentById(id);
        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student);
        Course course = courseRepository.findCourseById(courseId);
        course.setStudents(studentSet);
        courseRepository.save(course);
//        scRepository.save(sc);
//        System.out.println("id="+id+"courseID="+courseId);
//         new MyResult();
        MyResult myResult = new MyResult();
        int result = 1;
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("选课成功");
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @PostMapping("/addCourse")
    public MyResult addCourse(@RequestBody Course course){
        MyResult myResult = new MyResult();
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
        return course1;
    }

//    @ResponseBody
//    @PostMapping("/editCourseById/{id}")
//    public MyResult editCourseById(@PathVariable("id") int id,
//                                 @RequestBody CourseDTO editForm){
//        System.out.println("id="+id+" name="+editForm.getName()+" useBook="+editForm.getUseBook());
//        int result = courseRepository.update(editForm.getName(),editForm.getUseBook(),id);
//        System.out.println("result= "+result);
//        MyResult myResult = new MyResult();
//        if(result == 1){
//            myResult.setCode(200);
//            myResult.setMsg("修改成功");
//            return myResult;
//        }
//        return null;
//    }

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
