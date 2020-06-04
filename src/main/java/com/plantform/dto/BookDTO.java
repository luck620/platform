package com.plantform.dto;

public class BookDTO {
    String name;
    String publish;
    String type;
    String publishTime;
    String publishTimeStart;
    String publishTimeEnd;
    String author;
    String isbn;
    String introduction;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishTimeStart() {
        return publishTimeStart;
    }

    public void setPublishTimeStart(String publishTimeStart) {
        this.publishTimeStart = publishTimeStart;
    }

    public String getPublishTimeEnd() {
        return publishTimeEnd;
    }

    public void setPublishTimeEnd(String publishTimeEnd) {
        this.publishTimeEnd = publishTimeEnd;
    }
//
//    public String[] getAddTimeStartAndEnd() {
//        return addTimeStartAndEnd;
//    }
//
//    public void setAddTimeStartAndEnd(String[] addTimeStartAndEnd) {
//        this.addTimeStartAndEnd = addTimeStartAndEnd;
//    }
//
//    public String[] getPublishTimeStartAndEnd() {
//        return publishTimeStartAndEnd;
//    }
//
//    public void setPublishTimeStartAndEnd(String[] publishTimeStartAndEnd) {
//        this.publishTimeStartAndEnd = publishTimeStartAndEnd;
//    }
}
