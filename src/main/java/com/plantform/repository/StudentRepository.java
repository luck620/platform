package com.plantform.repository;

import com.plantform.entity.Account;
import com.plantform.entity.Course;
import com.plantform.entity.Student;
import com.plantform.entity.Teacher;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    @Query(nativeQuery = true, value = "select s.* from student s where s.phone=?1 and s.password=?2")
    Student getStudentBy(String phone, String password);

    @Query(nativeQuery = true, value = "select s.* from student s where s.id=?1")
    Student checkInfo(int id);

    @Query(nativeQuery = true,value = "select s.* from student s inner join sc c on c.student_id = s.id where c.course_id = ?1 " +
            "and case when ?2='' then 1=1 else s.name like %?2% end " +
            "and case when ?3='' then 1=1 else s.sno like %?3% end " +
            "and case when ?4='' then 1=1 else s.phone like %?4% end " +
            "and case when ?5='' then 1=1 else s.grade like %?5% end ")
    List<Student> findStudentByCourseId(int id, String name, String sno, String phone, String grade);

    @Query(nativeQuery = true,value = "select count(s.id) from student s inner join sc c on c.student_id = s.id where c.course_id = ?1 " +
            "and case when ?2='' then 1=1 else s.name like %?2% end " +
            "and case when ?3='' then 1=1 else s.sno like %?3% end " +
            "and case when ?4='' then 1=1 else s.phone like %?4% end " +
            "and case when ?5='' then 1=1 else s.grade like %?5% end ")
    int findStudentByCourseIdCount(int id, String name, String sno, String phone, String grade);


    @Query(nativeQuery = true, value="select s.* from student s")
    List<Student> findStudentAll();

    @Query(nativeQuery = true, value="select count(s.id) from student s")
    int findStudentAllCount();

    @Query(nativeQuery = true, value = "SELECT s.* from student s " +
            "where case when ?1='' then 1=1 else s.name like %?1% end " +
            "and case when ?2='' then 1=1 else s.sno like %?2% end " +
            "and case when ?3='' then 1=1 else s.phone like %?3% end " +
            "and case when ?4='' then 1=1 else s.mail like %?4% end " +
            "and case when ?5='' then 1=1 else s.grade like %?5% end ")
    List<Student> findAllByOthers(String name, String sno, String phone, String mail, String grade);

    @Query(nativeQuery = true, value = "SELECT count(s.id) from student s " +
            "where case when ?1='' then 1=1 else s.name like %?1% end " +
            "and case when ?2='' then 1=1 else s.sno like %?2% end " +
            "and case when ?3='' then 1=1 else s.phone like %?3% end " +
            "and case when ?4='' then 1=1 else s.mail like %?4% end " +
            "and case when ?5='' then 1=1 else s.grade like %?5% end ")
    int findAllByOthersCount(String name, String sno, String phone, String mail, String grade);


    @Query(nativeQuery = true, value = "select s.* from student s where s.id = ?1")
    Student findStudentById(int id);

    @Query(nativeQuery = true, value = "update student s set s.name=?1,s.sno=?2,s.phone=?3,s.mail=?4,s.grade=?5,s.password=?6,s.image_url=?7 where s.id=?8 ")
    @Modifying
    @Transactional
    int update(String name, String sno, String phone, String mail, String grade, String password,String imageUrl, int id);

    @Query(nativeQuery = true, value = "update student s set s.name=?1,s.sno=?2,s.mail=?3 where s.id=?4 ")
    @Modifying
    @Transactional
    int consummate(String name, String sno, String mail, int id);

    @Query(nativeQuery = true, value = "update student s set s.image_url=?1 where s.id=?2 ")
    @Modifying
    @Transactional
    int changeHead(String imageUrl , int id);

}
