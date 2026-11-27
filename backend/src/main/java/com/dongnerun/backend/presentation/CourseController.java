package com.dongnerun.backend.presentation;

import com.dongnerun.backend.data.RunningCourse;
import com.dongnerun.backend.service.CourseRecommendService;
import com.dongnerun.backend.service.CourseService;
import com.dongnerun.backend.service.dto.CourseRecommendResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    private static final int MAX_TARGET_KM = 50;

    private final CourseService courseService;
    private final CourseRecommendService recommendService;

    public CourseController(CourseService courseService, CourseRecommendService recommendService) {
        this.courseService = courseService;
        this.recommendService = recommendService;
    }

    @GetMapping("/api/courses")
    public List<RunningCourse> getCourses() {
        return courseService.findAll();
    }

    /**
     * 예: GET /api/courses/recommend?lat=37.5284&lng=126.9331&targetKm=5
     * 잘못된 값은 IllegalArgumentException을 던지고, GlobalExceptionHandler가 400으로 바꿔 응답한다.
     */
    @GetMapping("/api/courses/recommend")
    public List<CourseRecommendResponse> recommend(@RequestParam double lat,
                                                   @RequestParam double lng,
                                                   @RequestParam int targetKm) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new IllegalArgumentException("위도·경도 범위가 올바르지 않습니다.");
        }
        if (targetKm <= 0 || targetKm > MAX_TARGET_KM) {
            throw new IllegalArgumentException("목표 거리는 1~50km 사이여야 합니다.");
        }
        return recommendService.recommend(lat, lng, targetKm * 1000);
    }
}