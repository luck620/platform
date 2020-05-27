package com.plantform.repository;

import com.plantform.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import javax.transaction.Transactional;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value="insert into course(courseno,name,description,image_url,week_num,period_num,teacher_id) values(?1,?2,?3,?4,?5,?6,?7)")
    int addCourseByTeacher(String courseNO,String name,String description,String imageUrl,int weekNum,int periodNum,int id);

    @Query(nativeQuery = true, value="select c.id from course c where c.courseno = ?1")
    List<Integer> findByCourseNO(String courseNO);


    @Query(nativeQuery = true, value="select c.* from course c")
    List<Course> findCourseAll();

    @Query(nativeQuery = true, value="select count(c.id) from course c")
    int findCourseAllCount();

    @Query(nativeQuery = true, value = "SELECT c.* from course c " +
            "where case when ?1='' then 1=1 else c.name like %?1% end " +
            "and case when ?2='' then 1=1 else c.use_book like %?2% end " +
            "and case when ?3='' then 1=1 else c.stu_number between ?3 and ?4 end ")
    List<Course> findAllByOthers(String name,String useBook, int numberStart, int numberEnd);

    @Query(nativeQuery = true, value = "SELECT count(c.id) from course c " +
            "where case when ?1='' then 1=1 else c.name like %?1% end " +
            "and case when ?2='' then 1=1 else c.use_book like %?2% end " +
            "and case when ?3='' then 1=1 else c.stu_number between ?3 and ?4 end ")
    int findAllByOthersCount(String name,String useBook, int numberStart, int numberEnd);

    @Query(nativeQuery = true, value = "select c.* from course c where c.id = ?1")
    Course findCourseById(int id);

    @Query(nativeQuery = true, value = "select c.* from course c where c.name = ?1")
    Course findCourseByName(String courseName);

    @Query(nativeQuery = true, value = "update course c set c.name=?1,c.use_book=?2 where c.id=?3 ")
    @Modifying
    @Transactional
    int update(String name, String useBook,int id);
}
