package com.plantform.dto;

import com.plantform.entity.Student;
import com.plantform.entity.Teacher;
import java.util.List;

public class CourseDetailDTO {
    int id;
    int stuNumber;
    String name;
    List<Teacher> teacherList;
    List<Student> studentList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(int stuNumber) {
        this.stuNumber = stuNumber;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
