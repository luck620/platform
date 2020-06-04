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
    @Query(nativeQuery = true,value = "select count(b.id) from book b")
    int findAllCount();

    @Query(nativeQuery = true,value = "select distinct b.type from book b")
    List<String> findAllType();

    @Query(nativeQuery = true,value = "select b.* from book b where b.type = ?1")
    List<Book> findAllByType(String type);

    @Query(nativeQuery = true,value = "select b.* from book b where b.name like %?1% ")
    List<Book> findBooksByKeyWords(String name);

    @Query(nativeQuery = true,value = "select count(b.id) from book b where b.name like %?1% ")
    int findBooksByKeyWordsCount(String name);

    @Query(nativeQuery = true,value = "select count(b.id) from book b where b.type = ?1")
    int findAllByTypeCount(String type);

    @Query(nativeQuery = true, value = "SELECT b.* FROM book b " +
            "WHERE CASE WHEN ?1='' THEN 1=1 ELSE b.name LIKE %?1% END " +
            "AND CASE WHEN ?2='' THEN 1=1 ELSE b.publish LIKE %?2% END " +
            "AND CASE WHEN ?3='' THEN 1=1 ELSE b.type LIKE %?3% END " +
            "AND CASE WHEN ?4='' THEN 1=1 ELSE b.author like %?4% end " +
            "and case when ?5='' then 1=1 else b.isbn like %?5% end " +
            "AND CASE WHEN ?6='' THEN 1=1 ELSE b.publish_time BETWEEN ?6 AND ?7 END ")
    List<Book> findAllByOthers(String name, String publish, String type, String author,String isbn,String publishTimeStart,String publishTimeEnd);

    @Query(nativeQuery = true, value = "SELECT count(b.id) FROM book b " +
            "WHERE CASE WHEN ?1='' THEN 1=1 ELSE b.name LIKE %?1% END " +
            "AND CASE WHEN ?2='' THEN 1=1 ELSE b.publish LIKE %?2% END " +
            "AND CASE WHEN ?3='' THEN 1=1 ELSE b.type LIKE %?3% END " +
            "AND CASE WHEN ?4='' THEN 1=1 ELSE b.author like %?4% end " +
            "and case when ?5='' then 1=1 else b.isbn like %?5% end " +
            "AND CASE WHEN ?6='' THEN 1=1 ELSE b.publish_time BETWEEN ?6 AND ?7 END ")
    int findAllByOthersCount(String name, String publish, String type, String author,String isbn,String publishTimeStart,String publishTimeEnd);

    @Query(nativeQuery = true, value = "select b.* from book b where b.id = ?1")
    Book findBookById(int id);

    @Query(nativeQuery = true, value = "update book b set b.name=?1,b.publish=?2,b.type=?3,b.author=?4,b.isbn=?5,b.introduction=?6,b.publish_time=?7,b.image_url=?8 where b.id=?9 ")
    @Modifying
    @Transactional
    int update(String name, String publish, String type, String author, String isbn, String introduction, String publishTime, String imageUrl, int id);
}
