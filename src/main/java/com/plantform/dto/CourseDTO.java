package com.plantform.dto;

public class CourseDTO {
    int id;
    String name;
    String useBook;
    int numberStart;
    int numberEnd;

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

    public String getUseBook() {
        return useBook;
    }

    public void setUseBook(String useBook) {
        this.useBook = useBook;
    }

    public int getNumberStart() {
        return numberStart;
    }

    public void setNumberStart(int numberStart) {
        this.numberStart = numberStart;
    }

    public int getNumberEnd() {
        return numberEnd;
    }

    public void setNumberEnd(int numberEnd) {
        this.numberEnd = numberEnd;
    }
}
