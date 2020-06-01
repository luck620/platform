package com.plantform.repository;

import com.plantform.entity.Notice;
import com.plantform.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    @Query(nativeQuery = true, value = "select n.* from notice n inner join course c on c.id = n.course_id where n.title is not null and c.teacher_id=?1")
    List<Notice> getCourseNotice(int id);

    @Query(nativeQuery = true, value = "select count(n.id) from notice n inner join course c on c.id = n.course_id where n.title is not null and c.teacher_id=?1")
    int getCourseNoticeCount(int id);

    @Query(nativeQuery = true, value = "select n.* from notice n inner join course c on c.id = n.course_id inner join sc s on s.course_id = c.id where n.title is not null and s.student_id=?1")
    List<Notice> getCourseStuNotice(int id);

    @Query(nativeQuery = true, value = "select count(n.id) from notice n inner join course c on c.id = n.course_id inner join sc on sc.course_id = c.id where n.title is not null and sc.student_id=?1")
    int getCourseStuNoticeCount(int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into notice(content,date,title,word_url,course_id) values(?1,?2,?3,?4,?5)")
    int addCourseNotice(String content, LocalDateTime date, String title, String wordUrl, int id);

    @Query(nativeQuery = true, value = "select n.* from notice n where n.id = ?1")
    Notice findNoticeById(int id);

}
