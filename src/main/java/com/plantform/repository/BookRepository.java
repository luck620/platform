package com.plantform.repository;

import com.plantform.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(nativeQuery = true,value = "select distinct b.type from book b")
    List<String> findAllType();

    @Query(nativeQuery = true, value = "SELECT b.* FROM book b " +
            "WHERE CASE WHEN ?1='' THEN 1=1 ELSE b.name LIKE %?1% END " +
            "AND CASE WHEN ?2='' THEN 1=1 ELSE b.publish LIKE %?2% END " +
            "AND CASE WHEN ?3='' THEN 1=1 ELSE b.type LIKE %?3% END " +
            "AND CASE WHEN ?4='' THEN 1=1 ELSE b.add_time BETWEEN ?4 AND ?5 END " +
            "AND CASE WHEN ?6='' THEN 1=1 ELSE b.publish_time BETWEEN ?6 AND ?7 END ")
    Page<Book> findAllByOthers(String name, String publish, String type, String addTimeStart,String addTimeEnd,String publishTimeStart,String publishTimeEnd, Pageable pageable);

    @Query(nativeQuery = true, value = "select b.* from book b where b.id = ?1")
    Book findBookById(int id);

    @Query(nativeQuery = true, value = "update book b set b.name=?1,b.publish=?2,b.type=?3,b.add_time=?4,b.publish_time=?5 where b.id=?6 ")
    @Modifying
    @Transactional
    int update(String name, String publish, String type, String addTime,String publishTime,int id);
}
