package com.plantform.repository;

import com.plantform.entity.Children;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildrenRepository extends JpaRepository<Children,Integer> {
    @Query(nativeQuery = true,value = "select c.* from children c where c.menu_id = ?1")
    List<Children> findChildrenByMenu(int menuId);
}