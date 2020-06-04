package com.plantform.controller;

import com.plantform.dto.ThemeDTO;
import com.plantform.dto.ThemeDetailDTO;
import com.plantform.entity.Theme;
import com.plantform.entity.ThemeDetail;
import com.plantform.repository.ThemeDetailRepository;
import com.plantform.repository.ThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Resource
    ThemeRepository themeRepository;

    @Resource
    ThemeDetailRepository themeDetailRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list, int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    //查找类型为important的专题
    @ResponseBody
    @GetMapping("/getThemeByImportant")
    public List<ThemeDTO> getThemeByImportant(){
        List<Theme> themeList =  themeRepository.findThemeByImportant();
        List<ThemeDTO> themeDTOList = new ArrayList<>();
        if(themeList != null && !themeList.isEmpty()){
            for(Theme theme : themeList){
                ThemeDTO themeDTO = new ThemeDTO();
                themeDTO.setId(theme.getId());
                themeDTO.setImageUrl(theme.getImageUrl());
                themeDTO.setTitle(theme.getTitle());
                themeDTO.setType(theme.getType());
                themeDTO.setThemeDetailTitle(themeRepository.findThemeDetailTitle(theme.getId()));
                themeDTOList.add(themeDTO);
            }
        }
        return themeDTOList;
    }

    //查找所有
    @ResponseBody
    @GetMapping("/getThemeAll/{pageNum}/{pageSize}")
    public Page<ThemeDTO> getThemeAll(@PathVariable("pageNum") Integer pageNum,
                                   @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        int totalElements = themeRepository.findThemeAllCount();
        List<Theme> themeList =  themeRepository.findAll();
        List<ThemeDTO> themeDTOList = new ArrayList<>();
        if(themeList != null && !themeList.isEmpty()){
            for(Theme theme : themeList){
                ThemeDTO themeDTO = new ThemeDTO();
                themeDTO.setId(theme.getId());
                themeDTO.setImageUrl(theme.getImageUrl());
                themeDTO.setTitle(theme.getTitle());
                themeDTO.setType(theme.getType());
                themeDTOList.add(themeDTO);
            }
        }
        Page<ThemeDTO> themePage = listConvertToPage1(themeDTOList,totalElements,pageable);
        return themePage;
    }

    //查找类型为others的专题
    @ResponseBody
    @GetMapping("/getThemeByOthers")
    public List<Theme> getThemeByOthers(){
        List<Theme> themeList =  themeRepository.findThemeByOthers();
        List<Theme> themeList1 = new ArrayList<>();
        if(themeList != null && !themeList.isEmpty()){
            for(Theme theme : themeList){
                Theme theme1 = new Theme();
                theme1.setId(theme.getId());
                theme1.setImageUrl(theme.getImageUrl());
                theme1.setThemeDetails(theme.getThemeDetails());
                theme1.setTitle(theme.getTitle());
                theme1.setType(theme.getType());
                themeList1.add(theme1);
            }
        }
        return themeList1;
    }

    @ResponseBody
    @GetMapping("/getyqList/{type}")
    public List<ThemeDetailDTO> getyqList(@PathVariable("type") String type){
        System.out.println("type="+type);
        List<ThemeDetail> themeDetailList = themeDetailRepository.getyqList(type);
        List<ThemeDetailDTO> themeDetailDTOList = new ArrayList<>();
        if(themeDetailList!=null && !themeDetailList.isEmpty()){
            for(ThemeDetail themeDetail : themeDetailList){
                ThemeDetailDTO themeDetailDTO = new ThemeDetailDTO();
                themeDetailDTO.setId(themeDetail.getId());
                themeDetailDTO.setAuthor(themeDetail.getAuthor());
                themeDetailDTO.setContent(themeDetail.getContent());
                themeDetailDTO.setImageUrl(themeDetail.getImageUrl());
                themeDetailDTO.setTitle(themeDetail.getTitle());
                themeDetailDTOList.add(themeDetailDTO);
            }
        }
        return themeDetailDTOList;
    }

    @ResponseBody
    @GetMapping("/getyqListById/{id}")
    public ThemeDetailDTO getyqListById(@PathVariable("id") int id){
        ThemeDetail themeDetail = themeDetailRepository.getyqListById(id);
        ThemeDetailDTO themeDetailDTO = new ThemeDetailDTO();
        themeDetailDTO.setId(themeDetail.getId());
        themeDetailDTO.setAuthor(themeDetail.getAuthor());
        themeDetailDTO.setContent(themeDetail.getContent());
        themeDetailDTO.setImageUrl(themeDetail.getImageUrl());
        themeDetailDTO.setTitle(themeDetail.getTitle());
        themeDetailDTO.setVideoUrl(themeDetail.getVideoUrl());
        return themeDetailDTO;
    }

    //根据themeid查找themeDetail
    @ResponseBody
    @GetMapping("/getThemeDetailById/{id}/{pageNum}/{pageSize}")
    public Page<ThemeDetailDTO> getThemeDetailById(@PathVariable("id") int id,
                                                   @PathVariable("pageNum") Integer pageNum,
                                                   @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<ThemeDetail> themeDetailList = themeDetailRepository.findThemeDetailById(id);
        List<ThemeDetailDTO> themeDetailDTOList = new ArrayList<>();
        int totalElements = themeDetailRepository.findThemeDetailByIdCount(id);
        if(themeDetailList.size()>0){
            for(ThemeDetail themeDetail : themeDetailList){
                ThemeDetailDTO themeDetailDTO = new ThemeDetailDTO();
                themeDetailDTO.setId(themeDetail.getId());
                themeDetailDTO.setAuthor(themeDetail.getAuthor());
                themeDetailDTO.setContent(themeDetail.getContent());
                themeDetailDTO.setImageUrl(themeDetail.getImageUrl());
                themeDetailDTO.setTitle(themeDetail.getTitle());
                themeDetailDTO.setVideoUrl(themeDetail.getVideoUrl());
                themeDetailDTOList.add(themeDetailDTO);
            }
        }
        Page<ThemeDetailDTO> themeDetailDTOPage = listConvertToPage1(themeDetailDTOList,totalElements,pageable);
        return themeDetailDTOPage;
    }
}
