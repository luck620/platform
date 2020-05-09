package com.plantform.entity;

import java.util.List;

public class MenuAndChildren {
    private int id;

    private String name;

    private List<Children> childrenList;

    @Override
    public String toString() {
        return "MenuAndChildren{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", childrenList=" + childrenList +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Children> getChildrenList() {
        return this.childrenList;
    }

    public void setChildrenList(List<Children> childrenList) {
        this.childrenList = childrenList;
    }

}
