package com.plantform.repository;

import com.plantform.dto.ThemeDetailDTO;
import com.plantform.entity.Theme;
import com.plantform.entity.ThemeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThemeDetailRepository extends JpaRepository<ThemeDetail,Integer> {
    @Query(nativeQuery = true,value = "select td.* from theme_detail td  where td.theme_id=1 and td.type=?1")
    List<ThemeDetail> getyqList(String type);

    @Query(nativeQuery = true,value = "select td.* from theme_detail td  where td.id=?1")
    ThemeDetail getyqListById(int id);

    @Query(nativeQuery = true,value = "select td.* from theme_detail td inner join theme t on td.theme_id=t.id where t.id=?1")
    List<ThemeDetail> findThemeDetailById(int id);

    @Query(nativeQuery = true,value = "select count(td.id) from theme_detail td inner join theme t on td.theme_id=t.id where t.id=?1")
    int findThemeDetailByIdCount(int id);
}
