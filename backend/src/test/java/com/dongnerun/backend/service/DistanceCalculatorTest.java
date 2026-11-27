package com.dongnerun.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceCalculatorTest {

    @Test
    @DisplayName("같은 지점이면 거리는 0이다")
    void samePoint() {
        assertThat(DistanceCalculator.metersBetween(37.5284, 126.9331, 37.5284, 126.9331))
                .isZero();
    }

    @Test
    @DisplayName("위도 1도 차이는 약 111.2km다")
    void oneDegreeOfLatitude() {
        // 2 * π * 6,371,000 / 360 ≈ 111,195m
        assertThat(DistanceCalculator.metersBetween(37.0, 127.0, 38.0, 127.0))
                .isCloseTo(111_195, within(1.0));
    }

    @Test
    @DisplayName("출발점과 도착점을 바꿔도 거리는 같다")
    void symmetric() {
        double forward = DistanceCalculator.metersBetween(37.5284, 126.9331, 37.5252, 126.9295);
        double backward = DistanceCalculator.metersBetween(37.5252, 126.9295, 37.5284, 126.9331);

        assertThat(forward).isCloseTo(backward, within(1e-6));
    }

    @Test
    @DisplayName("여의나루역에서 예시 트랙 출발점까지는 약 476m다")
    void sampleLocationToTrack() {
        assertThat(DistanceCalculator.metersBetween(37.5284, 126.9331, 37.5252, 126.9295))
                .isCloseTo(476, within(5.0));
    }
}
