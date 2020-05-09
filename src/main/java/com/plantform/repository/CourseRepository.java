package com.plantform.repository;

import com.plantform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

public interface CourseRepository extends JpaRepository<Course, In> {
}
