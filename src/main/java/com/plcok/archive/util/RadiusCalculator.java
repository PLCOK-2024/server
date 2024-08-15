package com.plcok.archive.util;

import org.springframework.data.util.Pair;

public class RadiusCalculator {

    public static Pair<Double, Double> calculateByDirection(Double baseLatitude, Double baseLongitude, Double distance, Double bearing) {
        Double radianLat = toRadian(baseLatitude);
        Double radianLong = toRadian(baseLongitude);
        Double radianAngle = toRadian(bearing);
        Double distanceRadius = distance / 6371.01;

        Double latitude = Math.asin(Math.sin(radianLat) * Math.cos(distanceRadius) +
                Math.cos(radianLat) * Math.sin(distanceRadius) * Math.cos(radianAngle));
        Double longitude = radianLong +
                Math.atan2(Math.sin(radianAngle) * Math.sin(distanceRadius) * Math.cos(radianLat),
                        Math.cos(distanceRadius) - Math.sin(radianLat) * Math.sin(latitude));
        longitude = (longitude + 540) % 360 - 180;

        return Pair.of(toDegree(latitude), toDegree(longitude));
    }

    private static Double toRadian(Double coordinate) {
        return coordinate * Math.PI / 180.0;
    }

    private static Double toDegree(Double coordinate) {
        return coordinate * 180.0 / Math.PI;
    }
}
