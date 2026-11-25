package com.dongnerun.backend.course.dto;

import com.dongnerun.backend.course.CourseType;
import com.dongnerun.backend.course.RunningCourse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 추천 결과 한 건. 코스 정보에 사용자 위치 기준 계산값을 더해 내려준다.
 *
 * @param distanceMeters        코스 기준 거리(m). 순환형은 한 바퀴, 왕복형은 편도
 * @param distanceToStartMeters 사용자 위치에서 출발점까지 직선거리(m, 반올림)
 */
public record CourseRecommendResponse(
        Long id,
        String name,
        CourseType type,
        double startLatitude,
        double startLongitude,
        int distanceMeters,
        String directions,
        int distanceToStartMeters,
        String mapUrl
) {

    public static CourseRecommendResponse of(RunningCourse course, double distanceToStartMeters) {
        return new CourseRecommendResponse(
                course.getId(),
                course.getName(),
                course.getType(),
                course.getStartLatitude(),
                course.getStartLongitude(),
                course.getDistanceMeters(),
                course.getDirections(),
                (int) Math.round(distanceToStartMeters),
                kakaoMapUrl(course)
        );
    }

    // 카카오맵 링크 형식: /link/map/{이름},{위도},{경도}
    private static String kakaoMapUrl(RunningCourse course) {
        String name = URLEncoder.encode(course.getName(), StandardCharsets.UTF_8).replace("+", "%20");
        return "https://map.kakao.com/link/map/%s,%s,%s".formatted(
                name, course.getStartLatitude(), course.getStartLongitude());
    }
}
