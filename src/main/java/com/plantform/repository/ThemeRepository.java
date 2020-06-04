package com.plantform.repository;

import com.plantform.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme,Integer> {
    @Query(nativeQuery = true,value = "select t.* from theme t where t.type='important'")
    List<Theme> findThemeByImportant();

    @Query(nativeQuery = true,value = "select td.title from theme_detail td where td.theme_id = ?1")
    List<String> findThemeDetailTitle(int id);

    @Query(nativeQuery = true,value = "select t.* from theme t where t.type='others'")
    List<Theme> findThemeByOthers();

    @Query(nativeQuery = true,value = "select count(t.id) from theme t ")
    int findThemeAllCount();

}
