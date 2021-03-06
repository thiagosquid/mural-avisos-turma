package com.muraldaturma.api.service;

import com.muraldaturma.api.exception.CourseNotFoundException;
import com.muraldaturma.api.model.Course;
import com.muraldaturma.api.repository.CourseRepository;
import com.muraldaturma.api.exception.CourseAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAll(){
        return courseRepository.findAll();
    }

    public Course getById(Integer id) throws CourseNotFoundException {
        boolean exists = courseRepository.existsById(id);
        if (exists) {
            Course courseFound = courseRepository.getById(id);
            return courseFound;
        } else {
            throw new CourseNotFoundException(id);
        }

    }

    public void create(Course course) throws CourseAlreadyExistsException {
        boolean exists = courseRepository.existsCourseByName(course.getName());
        if (!exists) {
            courseRepository.saveAndFlush(course);
        } else {
            throw new CourseAlreadyExistsException(course.getName());
        }
    }

    public void deleteById(Integer id) throws CourseNotFoundException {
        boolean exists = courseRepository.existsById(id);
        if (exists) {
            courseRepository.deleteById(id);
        } else {
            throw new CourseNotFoundException(id);
        }
    }

    public Course update(Integer id, Course course) throws CourseNotFoundException {
        Optional<Course> oldCourse = courseRepository.findById(id);
        if (oldCourse.isPresent()) {
            course.setId(oldCourse.get().getId());
            return courseRepository.save(course);
        }else{
            throw new CourseNotFoundException(id);
        }
    }
}
