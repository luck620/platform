package com.plantform.repository;

import com.plantform.entity.ThemeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThemeDetailRepository extends JpaRepository<ThemeDetail,Integer> {
    @Query(nativeQuery = true,value = "select td.* from theme_detail td  where td.theme_id=1 and td.type=?1")
    List<ThemeDetail> getyqList(String type);

    @Query(nativeQuery = true,value = "select td.* from theme_detail td  where td.id=?1")
    ThemeDetail getyqListById(int id);
}
