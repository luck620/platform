package com.plantform.repository;

import com.plantform.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Integer> {
    //主页-工作动态
    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='home'")
    Page<News> findAllByTypeImportant(Pageable pageable);

    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='home'")
    List<News> findAllByTypeImportant();

    @Query(nativeQuery = true, value="select n.* from news n where n.type='others' and n.page_type ='home'")
    Page<News> findAllByTypeOthers(Pageable pageable);

    //学术前沿
    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='academic'")
    List<News> findAllImportantAcademic();

    @Query(nativeQuery = true, value="select n.* from news n where n.type='others' and n.page_type ='academic'")
    List<News> findAllOthersAcademic();

    //教学研究
    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='teach'")
    List<News> findAllImportantTeach();

    //活动会议
    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='activity'")
    List<News> findAllImportantActivity();

    //高校专区
    @Query(nativeQuery = true, value="select n.* from news n where n.type='important' and n.page_type ='university'")
    List<News> findAllImportantUniversity();


    @Query(nativeQuery = true,value = "select n.* from news n where n.id = ?1")
    News findNewsById(int id);

    //查找马克思主义......
    @Query(nativeQuery = true, value = "select n.* from news n where n.type= ?1 and n.page_type='academic'")
    List<News> findMarksByType(String type);

    //查找教学研究
    @Query(nativeQuery = true, value = "select n.* from news n where n.type= ?1 and n.page_type='teach'")
    List<News> findTeachByType(String type);
}
