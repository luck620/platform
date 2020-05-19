package com.plantform.controller;

import com.plantform.entity.Theme;
import com.plantform.repository.ThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Resource
    ThemeRepository themeRepository;

    //查找类型为important的专题
    @ResponseBody
    @GetMapping("/getThemeByImportant/{pageNum}/{pageSize}")
    public Page<Theme> getThemeByImportant(@PathVariable("pageNum") int pageNum,
                                           @PathVariable("pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return themeRepository.findThemeByImportant(pageable);
    }

    //查找类型为others的专题
    @ResponseBody
    @GetMapping("/getThemeByOthers/{pageNum}/{pageSize}")
    public Page<Theme> getThemeByOthers(@PathVariable("pageNum") int pageNum,
                                           @PathVariable("pageSize") int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return themeRepository.findThemeByOthers(pageable);
    }
}
