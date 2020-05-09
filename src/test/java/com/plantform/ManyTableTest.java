package com.plantform;

import com.plantform.entity.Course;
import com.plantform.entity.Student;
import com.plantform.entity.Teacher;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class ManyTableTest {
    private Session session;

    @Test
    public void testManyToMany() {
        Teacher teacher1 = new Teacher();
        teacher1.setName("张老师");

        Teacher teacher2 = new Teacher();
        teacher1.setName("王老师");

        Teacher teacher3 = new Teacher();
        teacher1.setName("李老师");

        Course course1 = new Course();
        course1.setName("语文");
        teacher1.setCourse(course1);

        Course course2 = new Course();
        course1.setName("数学");

        Student student1 = new Student();
        student1.setName("小明");
        Student student2 = new Student();
        student1.setName("小刚");

        student1.getCourses().add(course1);
        student1.getCourses().add(course2);

        course2.getStudents().add(student2);
    }
}
