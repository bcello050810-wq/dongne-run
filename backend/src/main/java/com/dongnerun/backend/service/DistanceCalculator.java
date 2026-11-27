package com.dongnerun.backend.service;

/**
 * 위도·경도 두 지점 사이의 직선거리를 계산한다.
 * 지구를 반지름 6,371km인 구로 보고 하버사인 공식을 사용한다.
 */
public final class DistanceCalculator {

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    private DistanceCalculator() {
    }

    public static double metersBetween(double startLatitude, double startLongitude,
                                       double endLatitude, double endLongitude) {
        double startLat = Math.toRadians(startLatitude);
        double endLat = Math.toRadians(endLatitude);
        double latDifference = endLat - startLat;
        double lngDifference = Math.toRadians(endLongitude - startLongitude);

        double a = Math.pow(Math.sin(latDifference / 2), 2)
                + Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(lngDifference / 2), 2);

        return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(Math.min(a, 1)));
    }
}
