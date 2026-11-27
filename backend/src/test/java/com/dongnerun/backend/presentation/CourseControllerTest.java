package com.dongnerun.backend.presentation;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongnerun.backend.service.CourseRecommendService;
import com.dongnerun.backend.service.RunPlan;
import com.dongnerun.backend.service.dto.CourseRecommendResponse;
import java.util.List;

import com.dongnerun.backend.data.CourseType;
import com.dongnerun.backend.data.RunningCourse;
import com.dongnerun.backend.data.RunningCourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CourseControllerTest {

    private CourseRecommendService recommendService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        recommendService = mock(CourseRecommendService.class);
        CourseController controller = new CourseController(mock(RunningCourseRepository.class), recommendService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("위치와 목표 거리(km)를 받아 m로 바꿔 추천 결과를 돌려준다")
    void recommend() throws Exception {
        RunningCourse track = new RunningCourse("400m 트랙", CourseType.LOOP, 37.5252, 126.9295,
                400, true, "테스트용 평지", "테스트용 안내");
        CourseRecommendResponse response =
                CourseRecommendResponse.of(track, 476.9, RunPlan.of(CourseType.LOOP, 400, 5000));
        when(recommendService.recommend(37.5284, 126.9331, 5000)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/courses/recommend")
                        .param("lat", "37.5284")
                        .param("lng", "126.9331")
                        .param("targetKm", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("400m 트랙"))
                .andExpect(jsonPath("$[0].distanceToStartMeters").value(477))
                .andExpect(jsonPath("$[0].runPlan.description").value("12바퀴 + 200m"));
    }

    @Test
    @DisplayName("목표 거리가 0 이하이면 400을 돌려준다")
    void rejectsNonPositiveTarget() throws Exception {
        mockMvc.perform(get("/api/courses/recommend")
                        .param("lat", "37.5284")
                        .param("lng", "126.9331")
                        .param("targetKm", "0"))
                .andExpect(status().isBadRequest());

        verify(recommendService, never()).recommend(anyDouble(), anyDouble(), anyInt());
    }

    @Test
    @DisplayName("위도가 범위를 벗어나면 400을 돌려준다")
    void rejectsInvalidLatitude() throws Exception {
        mockMvc.perform(get("/api/courses/recommend")
                        .param("lat", "91")
                        .param("lng", "126.9331")
                        .param("targetKm", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("필수 파라미터가 빠지면 400을 돌려준다")
    void rejectsMissingParameter() throws Exception {
        mockMvc.perform(get("/api/courses/recommend")
                        .param("lat", "37.5284")
                        .param("lng", "126.9331"))
                .andExpect(status().isBadRequest());
    }
}