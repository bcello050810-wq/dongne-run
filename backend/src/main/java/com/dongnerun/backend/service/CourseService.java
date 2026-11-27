package com.dongnerun.backend.service;

import com.dongnerun.backend.data.RunningCourse;
import com.dongnerun.backend.data.RunningCourseRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 코스 조회를 담당한다. 컨트롤러가 저장소(data 층)를 직접 쓰지 않도록 중간에서 연결한다.
 */
@Service
public class CourseService {

    private final RunningCourseRepository repository;

    public CourseService(RunningCourseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RunningCourse> findAll() {
        return repository.findAllByOrderByIdAsc();
    }
}