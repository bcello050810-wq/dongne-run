package com.dongnerun.backend.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningCourseRepository extends JpaRepository<RunningCourse, Long> {
    List<RunningCourse> findAllByOrderByIdAsc();

    // 평지로 확인된 코스만 추천 대상으로 쓴다.
    List<RunningCourse> findByFlatTrue();
}
