package com.plantform.controller;

import com.plantform.entity.Children;
import com.plantform.entity.Menu;
import com.plantform.entity.MenuAndChildren;
import com.plantform.repository.ChildrenRepository;
import com.plantform.repository.MenuAndChildrenRepository;
import com.plantform.repository.MenuRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mac")
public class MenuAndChildrenController {
    @Resource
    ChildrenRepository childrenRepository;

    @Resource
    MenuRepository menuRepository;

    @ResponseBody
    @GetMapping("/findAll")
    public List<MenuAndChildren> findAll(){
        List<Menu> menuList = menuRepository.findAll();
        List<MenuAndChildren> menuAndChildrenList = new ArrayList<>();
        System.out.println("==================");
        for(Menu menu: menuList){
            System.out.println("menuId:"+menu.getId()+"    menuName:"+menu.getName());
        }
        System.out.println("==================");
        for(Menu menu: menuList){
            MenuAndChildren menuAndChildren = new MenuAndChildren();
            List<Children> childrenList = childrenRepository.findChildrenByMenu(menu.getId());
            menuAndChildren.setId(menu.getId());
            menuAndChildren.setName(menu.getName());
            menuAndChildren.setChildrenList(childrenList);
            menuAndChildrenList.add(menuAndChildren);
        }
        System.out.println("==================");
        for(MenuAndChildren menuAndChildren: menuAndChildrenList){
            System.out.println("menuId:"+menuAndChildren.getId()+"    menuName:"+menuAndChildren.getName() +
                    "childrenList:"+menuAndChildren.getChildrenList());
        }
        System.out.println("==================");
        return menuAndChildrenList;
    }
}
