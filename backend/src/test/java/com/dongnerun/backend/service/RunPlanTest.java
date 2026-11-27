package com.dongnerun.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.dongnerun.backend.data.CourseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RunPlanTest {

    @Nested
    @DisplayName("순환형 코스")
    class Loop {

        @Test
        @DisplayName("5km를 400m 트랙에서 달리면 12바퀴 + 200m")
        void lapsWithRemainder() {
            RunPlan plan = RunPlan.of(CourseType.LOOP, 400, 5000);

            assertThat(plan.repeats()).isEqualTo(12);
            assertThat(plan.remainderMeters()).isEqualTo(200);
            assertThat(plan.description()).isEqualTo("12바퀴 + 200m");
        }

        @Test
        @DisplayName("나누어떨어지면 바퀴 수만 안내한다")
        void exactLaps() {
            assertThat(RunPlan.of(CourseType.LOOP, 500, 3000).description()).isEqualTo("6바퀴");
        }

        @Test
        @DisplayName("목표가 한 바퀴보다 짧으면 남은 거리만 안내한다")
        void shorterThanOneLap() {
            RunPlan plan = RunPlan.of(CourseType.LOOP, 4000, 3000);

            assertThat(plan.repeats()).isZero();
            assertThat(plan.description()).isEqualTo("3000m");
        }
    }

    @Nested
    @DisplayName("왕복형 코스")
    class OutAndBack {

        @Test
        @DisplayName("5km를 편도 1km 산책로에서 달리면 2왕복 + 500m 지점에서 돌아오기")
        void roundTripsWithTurnaround() {
            RunPlan plan = RunPlan.of(CourseType.OUT_AND_BACK, 1000, 5000);

            assertThat(plan.repeats()).isEqualTo(2);
            assertThat(plan.remainderMeters()).isEqualTo(1000);
            assertThat(plan.description()).isEqualTo("2왕복 + 500m 지점에서 돌아오기");
        }

        @Test
        @DisplayName("나누어떨어지면 왕복 횟수만 안내한다")
        void exactRoundTrips() {
            assertThat(RunPlan.of(CourseType.OUT_AND_BACK, 1000, 10000).description()).isEqualTo("5왕복");
        }

        @Test
        @DisplayName("목표가 한 번 왕복보다 짧으면 반환 지점만 안내한다")
        void shorterThanOneRoundTrip() {
            RunPlan plan = RunPlan.of(CourseType.OUT_AND_BACK, 2500, 3000);

            assertThat(plan.repeats()).isZero();
            assertThat(plan.description()).isEqualTo("1500m 지점에서 돌아오기");
        }
    }
}