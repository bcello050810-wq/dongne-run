package com.dongnerun.backend.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningCourseRepository extends JpaRepository<RunningCourse, Long> {
    List<RunningCourse> findAllByOrderByIdAsc();
}
