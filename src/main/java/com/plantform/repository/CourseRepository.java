package com.plantform.repository;

import com.plantform.dto.SC;
import com.plantform.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import javax.transaction.Transactional;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(nativeQuery = true, value = "select s.* from sc s where s.course_id = ?1")
    List<SC> getCourseGradeDetail(int id);

    @Query(nativeQuery = true, value = "select count(s.id) from sc s where s.course_id = ?1")
    int getCourseGradeDetailCount(int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value="insert into course(courseno,name,description,image_url,week_num,period_num,teacher_id) values(?1,?2,?3,?4,?5,?6,?7)")
    int addCourseByTeacher(String courseNO,String name,String description,String imageUrl,int weekNum,int periodNum,int id);

    @Query(nativeQuery = true, value="select c.id from course c where c.courseno = ?1")
    List<Integer> findByCourseNO(String courseNO);

    @Query(nativeQuery = true, value="select c.* from course c where c.teacher_id = ?1")
    List<Course> findCourseAllByTeacherId(int id);

    @Query(nativeQuery = true, value="select count(c.id) from course c where c.teacher_id = ?1")
    int findCourseAllByTeacherIdCount(int id);

    @Query(nativeQuery = true, value="select c.* from course c inner join sc s on s.course_id = c.id where s.student_id = ?1")
    List<Course>  findCourseListWithStuId(int id);

    @Query(nativeQuery = true, value="select count(c.id) from course c inner join sc s on s.course_id = c.id where s.student_id = ?1")
    int findCourseListWithStuIdCount(int id);


    @Query(nativeQuery = true, value="select c.* from course c")
    List<Course> findCourseAll();

    @Query(nativeQuery = true, value="select c.* from course c inner join sc s on s.course_id = c.id where c.test_url is not null and s.student_id = ?1")
    List<Course> findCourseStuTest(int id);

    @Query(nativeQuery = true, value="select count(c.id) from course c inner join sc s on s.course_id = c.id where c.test_url is not null and s.student_id = ?1")
    int findCourseTestStuCount(int id);

    @Query(nativeQuery = true, value="select distinct(c.id) from course c inner join sc s on s.course_id = c.id inner join exam e on e.course_id=c.id where s.student_id = ?1")
    List<Integer> findCourseStuExam(int id);

    @Query(nativeQuery = true, value="select count(distinct(c.id)) from course c inner join sc s on s.course_id = c.id inner join exam e on e.course_id=c.id where s.student_id = ?1")
    int findCourseExamStuCount(int id);

    @Query(nativeQuery = true, value="select c.* from course c where c.test_url is not null and c.teacher_id = ?1")
    List<Course> findCourseTest(int id);

    @Query(nativeQuery = true, value="select count(c.id) from course c where c.test_url is not null and c.teacher_id = ?1")
    int findCourseTestCount(int id);

    @Query(nativeQuery = true, value="select distinct(c.id) from course c inner join exam e on e.course_id=c.id where c.teacher_id = ?1")
    List<Integer> findCourseExam(int id);

    @Query(nativeQuery = true, value="select c.* from course c where c.stu_num is not null order by c.stu_num desc")
    List<Course> findCourseOrderBy();

    @Query(nativeQuery = true, value="select count(c.id) from course c where c.stu_num is not null order by c.stu_num desc")
    int findCourseOrderByCount();

    @Query(nativeQuery = true, value="select count(distinct(c.id)) from course c inner join exam e on e.course_id=c.id where c.teacher_id = ?1")
    int findCourseExamCount(int id);

    @Query(nativeQuery = true, value="select count(c.id) from course c")
    int findCourseAllCount();

    @Query(nativeQuery = true, value = "SELECT c.* from course c inner join teacher t on t.id = c.teacher_id " +
            "where case when ?1='' then 1=1 else c.name like %?1% end " +
            "and case when ?2='' then 1=1 else t.name like %?2% end " +
            "and case when ?3='' then 1=1 else c.stu_num between ?3 and ?4 end ")
    List<Course> findAllByOthers(String name,String teacherName, int numberStart, int numberEnd);

    @Query(nativeQuery = true, value = "SELECT count(c.id) from course c inner join teacher t on t.id = c.teacher_id  " +
            "where case when ?1='' then 1=1 else c.name like %?1% end " +
            "and case when ?2='' then 1=1 else t.name like %?2% end " +
            "and case when ?3='' then 1=1 else c.stu_num between ?3 and ?4 end ")
    int findAllByOthersCount(String name,String teacherName, int numberStart, int numberEnd);

    @Query(nativeQuery = true, value = "select c.* from course c where c.id = ?1")
    Course findCourseById(int id);

    @Query(nativeQuery = true, value = "select c.* from course c where c.name = ?1")
    Course findCourseByName(String courseName);

    @Query(nativeQuery = true, value = "update course c set c.name=?1,c.image_url=?2,c.courseno=?3,c.description=?4 where c.id=?5 ")
    @Modifying
    @Transactional
    int update(String name, String imageUrl, String courseNO,String description,int id);

    @Query(nativeQuery = true, value = "update course c set c.test_url=?1 where c.id=?2 ")
    @Modifying
    @Transactional
    int updateCourseTest(String wordUrl,int id);
}
