package com.plantform.repository;

import com.plantform.dto.TeacherDTO;
import com.plantform.entity.Book;
import com.plantform.entity.Course;
import com.plantform.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sun.security.krb5.internal.PAEncTSEnc;

import javax.transaction.Transactional;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    @Query(nativeQuery = true,value = "select distinct c.name courseName from course c")
    List<String> findAllType();

    @Query(nativeQuery = true, value="select t.* from teacher t")
    List<Teacher> findTeacherAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT t.* from teacher t inner join course c on c.id = t.course_id " +
            "where case when ?1='' THEN 1=1 ELSE t.name like %?1% END " +
            "AND CASE WHEN ?2='' THEN 1=1 ELSE t.phone like %?2% END " +
            "AND CASE WHEN ?3='' THEN 1=1 ELSE t.tno like %?3% END " +
            "AND CASE WHEN ?4='' THEN 1=1 ELSE t.mail like %?4% END " +
            "AND CASE WHEN ?5='' THEN 1=1 ELSE c.name like %?5% END ")
    List<Teacher> findAllByOthers(String name, String phone, String tno, String mail, String courseName, Pageable pageable);

    @Query(nativeQuery = true, value = "select t.* from teacher t where t.id = ?1")
    Teacher findTeacherById(int id);

    @Query(nativeQuery = true, value = "update teacher t set t.name=?1,t.phone=?2,t.tno=?3,t.mail=?4,t.password=?5,t.course_id=?6 where t.id=?7 ")
    @Modifying
    @Transactional
    int update(String name, String phone, String tno, String mail, String password, Course course, int id);
}
