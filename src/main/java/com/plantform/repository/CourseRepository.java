package com.plantform.repository;

import com.plantform.entity.Course;
import com.plantform.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import javax.transaction.Transactional;

public interface CourseRepository extends JpaRepository<Course, In> {
    @Query(nativeQuery = true, value = "SELECT c.* from course c " +
            "where case when ?1='' then 1=1 else c.name like %?1% end " +
            "and case when ?2='' then 1=1 else c.stu_number between ?2 and ?3 end ")
    Page<Course> findAllByOthers(String name, int numberStart, int numberEnd, Pageable pageable);

    @Query(nativeQuery = true, value = "select c.* from course c where c.id = ?1")
    Course findCourseById(int id);

    @Query(nativeQuery = true, value = "select c.* from course c where c.name = ?1")
    Course findCourseByName(String courseName);

    @Query(nativeQuery = true, value = "update course c set c.name=?1,c.use_book=?2,c.stuNumber=?3 where c.id=?7 ")
    @Modifying
    @Transactional
    int update(String name, String useBook, int stuNumber,int id);
}
