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

    @Resource
    ExamRepository examRepository;

    @Resource
    ScRepository scRepository;

    //教师添加测验
    @ResponseBody
    @PostMapping("/addCourseExam")
    public MyResult addCourseExam(@RequestBody ExamDTO examDTO){
        System.out.println("选择题数量="+examDTO.getType1Num()+"填空题数量="+examDTO.getType2Num()+"课程id="+examDTO.getCourseId());
        if(examDTO.getType1Num() > 0 ){
            for(int i=1; i<=examDTO.getType1Num(); i++){
                examRepository.addExamByType1("选择题",examDTO.getCourseId());
            }
        }
        if(examDTO.getType2Num() > 0 ){
            for(int i=1; i<=examDTO.getType2Num(); i++){
                examRepository.addExamByType2("判断题",examDTO.getCourseId());
            }
        }
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("成功");
        return myResult;
    }


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

    //课程课时详情
    @ResponseBody
    @PostMapping("/findCourseDetailPeriod/{id}/{pageNum}/{pageSize}")
    public Page<Period> findCourseDetailPeriod(@PathVariable("id") int id,
                                             @PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Period> periodList = periodRepository.getPeriodList(id);//查询该课程的教师
        int totalElements = periodRepository.getPeriodListCount(id);
        List<Period> periodList1 = new ArrayList<>();//存放teacher的几个相关信息
        for(Period period : periodList){
            Period period1 = new Period();
            period1.setId(period.getId());
            period1.setWeekST(period.getWeekST());
            period1.setPeriodST(period.getPeriodST());
            period1.setVideoUrl(period.getVideoUrl());
            periodList1.add(period1);
        }
        Page<Period> periodPage = listConvertToPage1(periodList1,totalElements,pageable);
        return periodPage;
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

    //后端模糊查询
    @ResponseBody
    @PostMapping("/getCourseListByOthers/{pageNum}/{pageSize}")
    public Page<CourseDTO> getCourseListByOthers(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                      @RequestBody CourseDTO courseDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findAllByOthers(courseDTO.getName(),courseDTO.getTeacherName(),courseDTO.getNumberStart(),courseDTO.getNumberEnd());
        int totalElements = courseRepository.findAllByOthersCount(courseDTO.getName(),courseDTO.getTeacherName(),courseDTO.getNumberStart(),courseDTO.getNumberEnd());
        List<CourseDTO> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                CourseDTO courseDTO1 = new CourseDTO();
                courseDTO1.setId(course.getId());
                courseDTO1.setName(course.getName());
                courseDTO1.setImageUrl(course.getImageUrl());
                courseDTO1.setCourseNO(course.getCourseNO());
                courseDTO1.setDescription(course.getDescription());
                courseDTO1.setPeriodNum(course.getPeriodNum());
                courseDTO1.setWeekNum(course.getWeekNum());
                courseDTO1.setTestUrl(course.getTestUrl());
                courseDTO1.setStuNum(course.getStuNum());
                courseDTO1.setTeacherName(course.getTeacher().getName());
                courseList1.add(courseDTO1);
            }
        }
        Page<CourseDTO> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    @ResponseBody
    @GetMapping("/getCourseListHou/{pageNum}/{pageSize}")
    public Page<CourseDTO> getCourseListHou(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseAll();
        int totalElements = courseRepository.findCourseAllCount();
        List<CourseDTO> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(course.getId());
                courseDTO.setName(course.getName());
                courseDTO.setImageUrl(course.getImageUrl());
                courseDTO.setCourseNO(course.getCourseNO());
                courseDTO.setDescription(course.getDescription());
                courseDTO.setPeriodNum(course.getPeriodNum());
                courseDTO.setWeekNum(course.getWeekNum());
                courseDTO.setTestUrl(course.getTestUrl());
                courseDTO.setStuNum(course.getStuNum());
                courseDTO.setTeacherName(course.getTeacher().getName());
                courseList1.add(courseDTO);
            }
        }
        Page<CourseDTO> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    @ResponseBody
    @GetMapping("/getCourseListWithoutPage")
    public List<CourseDTO> getCourseList(){
        List<Course> courseList =  courseRepository.findAll();
        List<CourseDTO> courseList1 = new ArrayList<>();
        if(courseList!=null &&!courseList.isEmpty()){
            for(Course course: courseList){
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(course.getId());
                courseDTO.setName(course.getName());
                courseDTO.setImageUrl(course.getImageUrl());
                courseDTO.setCourseNO(course.getCourseNO());
                courseDTO.setDescription(course.getDescription());
                courseDTO.setPeriodNum(course.getPeriodNum());
                courseDTO.setWeekNum(course.getWeekNum());
                courseDTO.setTestUrl(course.getTestUrl());
                courseDTO.setTeacherName(course.getTeacher().getName());
                courseList1.add(courseDTO);
            }
        }
        return courseList1;
    }

    //选择课程上传测验或公告
    @ResponseBody
    @GetMapping("/getCourseListByTeacher/{id}")
    public List<Course> getCourseListByTeacher(@PathVariable("id") int id){
        List<Course> courseList =  courseRepository.findCourseByTeacher(id);
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
        List<Integer> courseIdList =  courseRepository.findCourseStuExam(id);
        int totalElements = courseRepository.findCourseExamStuCount(id);
        List<Course> courseList = new ArrayList<>();
        if(courseIdList.size() > 0 ){
            for(Integer courseId: courseIdList){
                Course course = courseRepository.findCourseById(courseId);
                courseList.add(course);
            }
        }
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

    //教师获取测验资源
    @ResponseBody
    @GetMapping("/getCourseTestByTeaId/{pageNum}/{pageSize}/{id}")
    public Page<Course> getCourseTestByTeaId(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("id") int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Integer> courseIdList =  courseRepository.findCourseExam(id);
        List<Course> courseList = new ArrayList<>();
        if(courseIdList.size() > 0 ){
            for(Integer courseId: courseIdList){
                Course course = courseRepository.findCourseById(courseId);
                courseList.add(course);
            }
        }

        int totalElements = courseRepository.findCourseExamCount(id);
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

    //按照选课人数排课程
    @ResponseBody
    @GetMapping("/getCourseListOrderBy/{pageNum}/{pageSize}")
    public Page<Course> getCourseListOrderBy(@PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Course> courseList =  courseRepository.findCourseOrderBy();
        int totalElements = courseRepository.findCourseOrderByCount();
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
                course1.setStuNum(course.getStuNum());
                courseList1.add(course1);
            }
        }
        Page<Course> coursePage = listConvertToPage1(courseList1, totalElements, pageable);
        return coursePage;
    }

    //查看课程学生成绩
    @ResponseBody
    @GetMapping("/getCourseGradeDetail/{pageNum}/{pageSize}/{id}")
    public Page<SC> getCourseGradeDetail(@PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("id")int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<sc> scList =  scRepository.getCourseGradeDetail(id);
        int totalElements = scRepository.getCourseGradeDetailCount(id);
        List<SC> scList1 = new ArrayList<>();
        if(scList!=null &&!scList.isEmpty()){
            for(sc sc: scList){
                SC sc1 = new SC();
                sc1.setcId(sc.getCourse().getId());
                sc1.setsId(sc.getStudent().getId());
                sc1.setScore(sc.getScore());
                sc1.setGrade(sc.getStudent().getGrade());
                sc1.setMail(sc.getStudent().getMail());
                sc1.setName(sc.getStudent().getName());
                sc1.setPhone(sc.getStudent().getPhone());
                sc1.setSno(sc.getStudent().getSno());
                scList1.add(sc1);
            }
        }
        Page<SC> scPage = listConvertToPage1(scList1, totalElements, pageable);
        return scPage;
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
        int result = scRepository.addSC(courseId,id);
//        Student student = studentRepository.findStudentById(id);
//        Set<Student> studentSet = new HashSet<>();
//        studentSet.add(student);
//        Course course = courseRepository.findCourseById(courseId);
//        if(course.getStuNum() == null){
//            course.setStuNum(1+"");
//        }
//        else{
//            course.setStuNum((Integer.valueOf(course.getStuNum())+1)+"");
//        }
//        course.setStudents(studentSet);
//        courseRepository.save(course);
//        scRepository.save(sc);
//        System.out.println("id="+id+"courseID="+courseId);
//         new MyResult();
        MyResult myResult = new MyResult();
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
        course1.setCourseNO(course.getCourseNO());
        course1.setDescription(course.getDescription());
        return course1;
    }

    @ResponseBody
    @PostMapping("/editCourseById/{id}")
    public MyResult editCourseById(@PathVariable("id") int id,
                                 @RequestBody CourseDTO editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" courseNO="+editForm.getCourseNO() +" imageUrl ="+editForm.getImageUrl()+" description="+editForm.getDescription());
        String imageUrl = "http://qaath1lbd.bkt.clouddn.com/"+editForm.getImageUrl();
        int result = courseRepository.update(editForm.getName(),imageUrl, editForm.getCourseNO(),editForm.getDescription(),id);
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
