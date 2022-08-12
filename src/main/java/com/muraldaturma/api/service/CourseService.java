package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.CourseDTO;
import com.muraldaturma.api.dto.mapper.CourseMapper;
import com.muraldaturma.api.exception.CourseAlreadyExistsException;
import com.muraldaturma.api.exception.CourseNotFoundException;
import com.muraldaturma.api.model.Course;
import com.muraldaturma.api.repository.CourseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    public List<CourseDTO> getAll() {
        return courseMapper.toListDTO(courseRepository.findAll());
    }

    public CourseDTO getById(Integer id) {
        Optional<Course> courseFound = courseRepository.findById(id);
        if (courseFound.isPresent()) {
            return courseMapper.toDTO(courseFound.get());
        } else {
            throw new CourseNotFoundException(String.format("Curso com id %d não encontrado.", id), "course.notFound");
        }
    }

    public CourseDTO create(CourseDTO courseDTO) {
        Course course = courseMapper.toModel(courseDTO);
        boolean exists = courseRepository.existsCourseByName(course.getName());
        if (!exists) {
            courseRepository.saveAndFlush(course);
            return courseMapper.toDTO(course);
        } else {
            throw new CourseAlreadyExistsException(String.format("Já existe um curso com o nome %s",course.getName()),"course.alreadyExist");
        }
    }

    public void deleteById(Integer id) {
        boolean exists = courseRepository.existsById(id);
        if (exists) {
            courseRepository.deleteById(id);
        } else {
            throw new CourseNotFoundException(String.format("Curso com id %d não encontrado.", id), "course.notFound");
        }
    }

    public CourseDTO update(Integer id, CourseDTO courseDTO) throws CourseNotFoundException {
        Course courseToUpdate = courseMapper.toModel(courseDTO);
        Optional<Course> oldCourse = courseRepository.findById(id);

        if (oldCourse.isPresent()) {
            BeanUtils.copyProperties(courseDTO, oldCourse.get(),"id");
            return courseMapper.toDTO(courseRepository.save(oldCourse.get()));
        } else {
            throw new CourseNotFoundException(String.format("Curso com id %d não encontrado.", id), "course.notFound");
        }
    }
}
