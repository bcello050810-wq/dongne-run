package com.dongnerun.backend.course;

import com.dongnerun.backend.course.dto.CourseRecommendResponse;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRecommendService {

    private final RunningCourseRepository repository;

    public CourseRecommendService(RunningCourseRepository repository) {
        this.repository = repository;
    }

    /**
     * 평지 코스를 사용자 위치에서 출발점까지 가까운 순으로 정렬해 돌려준다.
     */
    @Transactional(readOnly = true)
    public List<CourseRecommendResponse> recommend(double latitude, double longitude) {
        return repository.findByFlatTrue().stream()
                .map(course -> CourseRecommendResponse.of(course, DistanceCalculator.metersBetween(
                        latitude, longitude, course.getStartLatitude(), course.getStartLongitude())))
                .sorted(Comparator.comparingInt(CourseRecommendResponse::distanceToStartMeters))
                .toList();
    }
}
