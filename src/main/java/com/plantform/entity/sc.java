package com.plantform.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

@Entity
public class sc{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String score;

    @JSONField(serialize = false)
    @OneToOne
    Course course;

    @JSONField(serialize = false)
    @OneToOne
    Student student;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
