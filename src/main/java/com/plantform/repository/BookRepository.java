package com.plantform.repository;

import com.plantform.entity.Account;
import com.plantform.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface BookRepository extends JpaRepository<Book,Integer> {
    @Query(nativeQuery = true, value = "select b.* from book b " +
            "where case when ?1='' then 1=1 else b.name like %?1% end " +
            "and case when ?2='' then 1=1 else b.publish like %?2% end " +
            "and case when ?3='' then 1=1 else b.type like %?3% end " +
            "and case when ?4='' then 1=1 else b.add_time between ?4 and ?5 end " +
            "and case when ?6='' then 1=1 else b.publish_time between ?6 and ?7 end ")
    Page<Book> findAllByOthers(String name, String publish, String type, String addTimeStart,String addTimeEnd,String publishTimeStart,String publishTimeEnd, Pageable pageable);

    @Query(nativeQuery = true, value = "select b.* from book b where b.id = ?1")
    Book findBookById(int id);

    @Query(nativeQuery = true, value = "update book b set b.name=?1,b.publish=?2,b.type=?3,b.add_time=?4,b.publish_time=?5 where b.id=?6 ")
    @Modifying
    @Transactional
    int update(String name, String publish, String type, String addTime,String publishTime,int id);
}
