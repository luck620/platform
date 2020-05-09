package com.plantform.controller;

import com.plantform.entity.Menu;
import com.plantform.repository.MenuRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    MenuRepository menuRepository;

    @ResponseBody
    @GetMapping("/findAll")
    public List<Menu> findAll(){
        return menuRepository.findAll();
    }
}
