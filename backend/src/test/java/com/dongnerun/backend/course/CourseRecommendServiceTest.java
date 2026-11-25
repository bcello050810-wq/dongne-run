package com.dongnerun.backend.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dongnerun.backend.course.dto.CourseRecommendResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CourseRecommendServiceTest {

    // 예시 기준 위치: 여의나루역
    private static final double USER_LATITUDE = 37.5284;
    private static final double USER_LONGITUDE = 126.9331;

    private RunningCourseRepository repository;
    private CourseRecommendService service;

    @BeforeEach
    void setUp() {
        repository = mock(RunningCourseRepository.class);
        service = new CourseRecommendService(repository);
    }

    @Test
    @DisplayName("출발점이 가까운 코스부터 정렬한다")
    void sortsByDistanceToStart() {
        RunningCourse far = course("먼 코스", 37.5400, 126.9500);
        RunningCourse near = course("가까운 코스", 37.5291, 126.9366);
        RunningCourse middle = course("중간 코스", 37.5252, 126.9295);
        when(repository.findByFlatTrue()).thenReturn(List.of(far, near, middle));

        List<CourseRecommendResponse> result = service.recommend(USER_LATITUDE, USER_LONGITUDE);

        assertThat(result).extracting(CourseRecommendResponse::name)
                .containsExactly("가까운 코스", "중간 코스", "먼 코스");
        assertThat(result).extracting(CourseRecommendResponse::distanceToStartMeters)
                .isSorted();
    }

    @Test
    @DisplayName("코스 정보와 출발점까지 거리, 지도 링크를 담는다")
    void mapsCourseToResponse() {
        RunningCourse track = course("400m 트랙", 37.5252, 126.9295);
        when(repository.findByFlatTrue()).thenReturn(List.of(track));

        CourseRecommendResponse response = service.recommend(USER_LATITUDE, USER_LONGITUDE).getFirst();

        assertThat(response.name()).isEqualTo("400m 트랙");
        assertThat(response.type()).isEqualTo(CourseType.LOOP);
        assertThat(response.distanceMeters()).isEqualTo(400);
        assertThat(response.distanceToStartMeters()).isBetween(470, 480);
        assertThat(response.mapUrl())
                .isEqualTo("https://map.kakao.com/link/map/400m%20%ED%8A%B8%EB%9E%99,37.5252,126.9295");
    }

    @Test
    @DisplayName("평지 코스가 없으면 빈 목록을 돌려준다")
    void emptyWhenNoFlatCourse() {
        when(repository.findByFlatTrue()).thenReturn(List.of());

        assertThat(service.recommend(USER_LATITUDE, USER_LONGITUDE)).isEmpty();
    }

    private static RunningCourse course(String name, double latitude, double longitude) {
        return new RunningCourse(name, CourseType.LOOP, latitude, longitude,
                400, true, "테스트용 평지", "테스트용 안내");
    }
}
