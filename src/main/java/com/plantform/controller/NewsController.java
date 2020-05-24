package com.plantform.controller;

import com.plantform.dto.Image;
import com.plantform.entity.Course;
import com.plantform.entity.News;
import com.plantform.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsRepository newsRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list, int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    @ResponseBody
    @GetMapping("/findAllImportant/{pageNum}/{pageSize}")
    public Page<News> findAllImportant(@PathVariable("pageNum") Integer pageNum,
                @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return newsRepository.findAllByTypeImportant(pageable);
    }

    @ResponseBody
    @GetMapping("/findAllOthers/{pageNum}/{pageSize}")
    public Page<News> findAllOthers(@PathVariable("pageNum") Integer pageNum,
                              @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return newsRepository.findAllByTypeOthers(pageable);
    }

    //根据关键词搜索新闻信息
    @ResponseBody
    @GetMapping("/findAllByKeyWords/{pageNum}/{pageSize}/{newsKeyWords}")
    public Page<News> findAllByKeyWords(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @PathVariable("newsKeyWords") String newsKeyWords){
        if(newsKeyWords == null){
            newsKeyWords = "";
        }
        System.out.println("newsKeyWords="+newsKeyWords);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<News> newsList = newsRepository.findAllByKeyWords(newsKeyWords);
        int totalElements = newsRepository.findAllByKeyWordsCount(newsKeyWords);
        System.out.println("totalElements="+totalElements);
        List<News> newsList1 = new ArrayList<>();
        if(newsList!=null &&!newsList.isEmpty()){
            for(News news : newsList){
                News news1 = new News();
                news1.setId(news.getId());
                news1.setAuthor(news.getAuthor());
                news1.setContent(news.getContent());
                news1.setDate(news.getDate());
                news1.setSource(news.getSource());
                news1.setTitle(news.getTitle());
                news1.setType(news.getType());
                newsList1.add(news1);
            }
        }
        Page<News> newsPage = listConvertToPage1(newsList1, totalElements, pageable);
        return newsPage;
    }

    @ResponseBody
    @GetMapping("/findImages")
    public List<Image> findImages(){
         List<News> newsList = newsRepository.findAllByTypeImportant();
         List<Image> imageList = new ArrayList<>();
         if(newsList != null && !newsList.isEmpty())
         for(News news : newsList){
             Image image = new Image();
             image.setId(news.getId());
             image.setSrc(news.getImageUrl());
             imageList.add(image);
         }
         return imageList;
    }

    //学术前沿
    @ResponseBody
    @GetMapping("/findAllImportantAcademic")
    public List<News> findAllImportantAcademic(){
        return newsRepository.findAllImportantAcademic();
    }

    @ResponseBody
    @GetMapping("/findImagesAcademic")
    public List<Image> findImagesAcademic(){
        List<News> newsList = newsRepository.findAllImportantAcademic();
        List<Image> imageList = new ArrayList<>();
        if(newsList != null && !newsList.isEmpty())
            for(News news : newsList){
                Image image = new Image();
                image.setId(news.getId());
                image.setSrc(news.getImageUrl());
                imageList.add(image);
            }
        return imageList;
    }

    //根据id查找该新闻
    @ResponseBody
    @GetMapping("/findNewsById/{id}")
    public News findNewsById(@PathVariable("id") int id){
        return newsRepository.findNewsById(id);
    }

    //教学研究
    @ResponseBody
    @GetMapping("/findAllImportantTeach")
    public List<News> findAllImportantTeach(){
        return newsRepository.findAllImportantTeach();
    }

    @ResponseBody
    @GetMapping("/findImagesTeach")
    public List<Image> findImagesTeach(){
        List<News> newsList = newsRepository.findAllImportantTeach();
        List<Image> imageList = new ArrayList<>();
        if(newsList != null && !newsList.isEmpty())
            for(News news : newsList){
                Image image = new Image();
                image.setId(news.getId());
                image.setSrc(news.getImageUrl());
                imageList.add(image);
            }
        return imageList;
    }

    //活动会议
    @ResponseBody
    @GetMapping("/findAllImportantActivity")
    public List<News> findAllImportantActivity(){
        return newsRepository.findAllImportantActivity();
    }

    @ResponseBody
    @GetMapping("/findImagesActivity")
    public List<Image> findImagesActivity(){
        List<News> newsList = newsRepository.findAllImportantActivity();
        List<Image> imageList = new ArrayList<>();
        if(newsList != null && !newsList.isEmpty())
            for(News news : newsList){
                Image image = new Image();
                image.setId(news.getId());
                image.setSrc(news.getImageUrl());
                imageList.add(image);
            }
        return imageList;
    }

    //高校专区
    @ResponseBody
    @GetMapping("/findAllImportantUniversity")
    public List<News> findAllImportantUniversity(){
        return newsRepository.findAllImportantUniversity();
    }

    @ResponseBody
    @GetMapping("/findImagesUniversity")
    public List<Image> findImagesUniversity(){
        List<News> newsList = newsRepository.findAllImportantUniversity();
        List<Image> imageList = new ArrayList<>();
        if(newsList != null && !newsList.isEmpty())
            for(News news : newsList){
                Image image = new Image();
                image.setId(news.getId());
                image.setSrc(news.getImageUrl());
                imageList.add(image);
            }
        return imageList;
    }

    @ResponseBody
    @GetMapping("/findMarksByType/{type}")
    public List<News> findMarksByType(@PathVariable("type") String type) {
        System.out.println("type="+type);
        return newsRepository.findMarksByType(type);
    }

    @ResponseBody
    @GetMapping("/findTeachByType/{type}")
    public List<News> findTeachByType(@PathVariable("type") String type) {
        System.out.println("type="+type);
        return newsRepository.findTeachByType(type);
    }

    @ResponseBody
    @GetMapping("/findActivityByType/{type}")
    public List<News> findActivityByType(@PathVariable("type") String type) {
        System.out.println("type="+type);
        return newsRepository.findActivityByType(type);
    }

    @ResponseBody
    @GetMapping("/findUniversityByType/{type}")
    public List<News> findUniversityByType(@PathVariable("type") String type) {
        System.out.println("type="+type);
        return newsRepository.findUniversityByType(type);
    }
}
