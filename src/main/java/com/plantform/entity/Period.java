package com.plantform.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

@Entity
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String videoUrl;

    @Column
    private String weekST;//第几周

    @Column
    private String periodST;//第几课时

    @JSONField(serialize = false)
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getWeekST() {
        return weekST;
    }

    public void setWeekST(String weekST) {
        this.weekST = weekST;
    }

    public String getPeriodST() {
        return periodST;
    }

    public void setPeriodST(String periodST) {
        this.periodST = periodST;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
