package com.plantform.repository;

import com.plantform.dto.SC;
import com.plantform.entity.sc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ScRepository extends JpaRepository<sc,Integer> {

    @Query(nativeQuery = true, value = "select s.* from sc s where s.course_id = ?1")
    List<sc> getCourseGradeDetail(int id);

    @Query(nativeQuery = true, value = "select count(s.id) from sc s where s.course_id = ?1")
    int getCourseGradeDetailCount(int id);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into sc(course_id,student_id) values(?1,?2)")
    int addSC(int courseId, int id);

}
