package com.plantform.repository;

import com.plantform.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Integer> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "insert into period(periodst,weekst,course_id) values(?1,?2,?3)")
    int addPeriod(String periodST, String weekST, int id);

    @Query(nativeQuery = true, value = "select p.periodst from period p inner join course c on p.course_id=c.id where p.weekst=?1 and c.courseno=?2")
    List<String> getPeriodST(String weekST, String courseNO);

    @Query(nativeQuery = true, value = "select distinct(p.weekst) from period p inner join course c on p.course_id=c.id where c.courseno=?1")
    List<String> getWeekST(String courseNO);
}
