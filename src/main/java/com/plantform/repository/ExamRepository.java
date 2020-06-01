package com.plantform.repository;

import com.plantform.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into exam(type,course_id) values(?1,?2)")
    int addExamByType1(String type, int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into exam(type,course_id) values(?1,?2)")
    int addExamByType2(String type, int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update exam e set e.title=?1,e.choosea=?2,e.chooseb=?3,e.choosec=?4,e.choosed=?5,e.answer=?6,e.content=?7 where e.id=?8")
    int updateExamInfo(String title,String chooseA,String chooseB,String chooseC,String chooseD,String answer,String content, int id);

    @Query(nativeQuery = true, value = "select e.* from exam e where e.course_id = ?1 order by e.type desc")
    List<Exam> getExam(int id);

    @Query(nativeQuery = true, value = "select e.answer from exam e where e.id = ?1")
    String getCorrectAnswer(int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sc s set s.score = ?1 where s.student_id=?2 and s.course_id=?3")
    int addScore(int score,int id, int courseId);

    @Query(nativeQuery = true, value = "select s.score from sc s where s.student_id = ?1 and s.course_id = ?2")
    String hasScore(int id, int courseId);
}
