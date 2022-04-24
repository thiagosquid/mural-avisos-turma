package com.muraldaturma.api.repository;

import com.muraldaturma.api.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    boolean existsCourseByName(String name);
}
