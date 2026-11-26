package com.dongnerun.backend.course;

import com.dongnerun.backend.course.dto.CourseRecommendResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CourseController {

    private static final int MAX_TARGET_KM = 50;

    private final RunningCourseRepository repository;
    private final CourseRecommendService recommendService;

    public CourseController(RunningCourseRepository repository, CourseRecommendService recommendService) {
        this.repository = repository;
        this.recommendService = recommendService;
    }

    @GetMapping("/api/courses")
    public List<RunningCourse> getCourses() {
        return repository.findAllByOrderByIdAsc();
    }

    /**
     * 예: GET /api/courses/recommend?lat=37.5284&lng=126.9331&targetKm=5
     */
    @GetMapping("/api/courses/recommend")
    public List<CourseRecommendResponse> recommend(@RequestParam double lat,
                                                   @RequestParam double lng,
                                                   @RequestParam int targetKm) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "위도·경도 범위가 올바르지 않습니다.");
        }
        if (targetKm <= 0 || targetKm > MAX_TARGET_KM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "목표 거리는 1~50km 사이여야 합니다.");
        }
        return recommendService.recommend(lat, lng, targetKm * 1000);
    }
}