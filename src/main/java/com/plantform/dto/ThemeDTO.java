package com.plantform.dto;

import java.util.List;

public class ThemeDTO {
    private int id;
    private String imageUrl;
    private String type;
    private String title;
    private List themeDetailTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getThemeDetailTitle() {
        return themeDetailTitle;
    }

    public void setThemeDetailTitle(List themeDetailTitle) {
        this.themeDetailTitle = themeDetailTitle;
    }
}
