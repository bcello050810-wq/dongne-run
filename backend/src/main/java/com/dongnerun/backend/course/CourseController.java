package com.dongnerun.backend.course;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    private final RunningCourseRepository repository;

    public CourseController(RunningCourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/courses")
    public List<RunningCourse> getCourses() {
        return repository.findAllByOrderByIdAsc();
    }
}
