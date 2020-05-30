package com.plantform.controller;

import com.plantform.dto.NoticeDTO;
import com.plantform.entity.Course;
import com.plantform.entity.MyResult;
import com.plantform.entity.Notice;
import com.plantform.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    NoticeRepository noticeRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list, int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    @ResponseBody
    @GetMapping("/getCourseNotice/{pageNum}/{pageSize}/{id}")
    public Page<NoticeDTO> getCourseNotice(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize,
                                           @PathVariable("id")int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Notice> noticeList =  noticeRepository.getCourseNotice(id);
        int totalElements = noticeRepository.getCourseNoticeCount(id);
        List<NoticeDTO> noticeList1 = new ArrayList<>();
        if(noticeList!=null &&!noticeList.isEmpty()){
            for(Notice notice: noticeList){
                NoticeDTO noticeDTO = new NoticeDTO();
                noticeDTO.setId(notice.getId());
                noticeDTO.setContent(notice.getContent());
                noticeDTO.setTitle(notice.getTitle());
                noticeDTO.setDate(notice.getDate().toString().replace('T',' '));
                noticeDTO.setWordUrl(notice.getWordUrl());
                noticeDTO.setCourseId(notice.getCourse().getId());
                noticeDTO.setCourseName(notice.getCourse().getName());
                noticeList1.add(noticeDTO);
            }
        }
        Page<NoticeDTO> noticeDTOPage = listConvertToPage1(noticeList1, totalElements, pageable);
        return noticeDTOPage;
    }

    //学生获取公告
    @ResponseBody
    @GetMapping("/getCourseStuNotice/{pageNum}/{pageSize}/{id}")
    public Page<NoticeDTO> getCourseStuNotice(@PathVariable("pageNum") Integer pageNum,
                                           @PathVariable("pageSize") Integer pageSize,
                                           @PathVariable("id")int id){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Notice> noticeList =  noticeRepository.getCourseStuNotice(id);
        int totalElements = noticeRepository.getCourseStuNoticeCount(id);
        List<NoticeDTO> noticeList1 = new ArrayList<>();
        if(noticeList!=null &&!noticeList.isEmpty()){
            for(Notice notice: noticeList){
                NoticeDTO noticeDTO = new NoticeDTO();
                noticeDTO.setId(notice.getId());
                noticeDTO.setContent(notice.getContent());
                noticeDTO.setTitle(notice.getTitle());
                noticeDTO.setDate(notice.getDate().toString().replace('T',' '));
                noticeDTO.setWordUrl(notice.getWordUrl());
                noticeDTO.setCourseId(notice.getCourse().getId());
                noticeDTO.setCourseName(notice.getCourse().getName());
                noticeList1.add(noticeDTO);
            }
        }
        Page<NoticeDTO> noticeDTOPage = listConvertToPage1(noticeList1, totalElements, pageable);
        return noticeDTOPage;
    }

    //发布公告
    @ResponseBody
    @PostMapping("/addCourseNotice/{id}/{wordURL}")
    public MyResult addCourseNotice(@PathVariable("id")int id,
                                  @PathVariable("wordURL")String wordURL,
                                  @RequestBody NoticeDTO noticeDTO){
        String wordUrl = "http://qaath1lbd.bkt.clouddn.com/"+wordURL;
        System.out.println(id+wordUrl+noticeDTO.getContent()+noticeDTO.getDate()+noticeDTO.getTitle());
        MyResult myResult = new MyResult();
        int result = noticeRepository.addCourseNotice(noticeDTO.getContent(),LocalDateTime.parse(noticeDTO.getDate(),df),noticeDTO.getTitle(),wordUrl,id);
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("发布群公告成功");
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findNoticeById/{id}")
    public Notice findNoticeById(@PathVariable("id")int id){
        Notice notice = noticeRepository.findNoticeById(id);
        Notice notice1 = new Notice();
        notice1.setContent(notice.getContent());
        notice1.setDate(notice.getDate());
        notice1.setTitle(notice.getTitle());
        notice1.setWordUrl(notice.getWordUrl());
        return notice1;
    }
}
