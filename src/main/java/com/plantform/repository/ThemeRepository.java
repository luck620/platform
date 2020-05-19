package com.plantform.repository;

import com.plantform.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ThemeRepository extends JpaRepository<Theme,Integer> {
    @Query(nativeQuery = true,value = "select t.* from theme t where t.type='important'")
    Page<Theme> findThemeByImportant(Pageable pageable);

    @Query(nativeQuery = true,value = "select t.* from theme t where t.type='others'")
    Page<Theme> findThemeByOthers(Pageable pageable);
}
