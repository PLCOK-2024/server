package com.example.demo.archive.repository;

import com.example.demo.archive.util.CardinalDirection;
import com.example.demo.archive.util.RadiusCalculator;
import com.example.demo.common.entity.Archive;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@RequiredArgsConstructor
public class ArchiveRepositoryImpl implements ArchiveRepositoryCustom {
    private final EntityManager entityManager;
    private final double DISTANCE = 3;

    @Override
    public List<Archive> findNearArchives(double baseLatitude, double baseLongitude) {
        Pair<Double, Double> northEast = RadiusCalculator.calculateByDirection(baseLatitude, baseLongitude, DISTANCE, CardinalDirection.NORTHEAST.getBearing());
        Pair<Double, Double> southWest = RadiusCalculator.calculateByDirection(baseLatitude, baseLongitude, DISTANCE, CardinalDirection.SOUTHWEST.getBearing());

        double x1 = northEast.getSecond(); // longitude
        double y1 = northEast.getFirst();  // latitude
        double x2 = southWest.getSecond(); // longitude
        double y2 = southWest.getFirst();  // latitude

        String polygonWKT = String.format("POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                x2, y2,   // southWest
                x2, y1,   // northWest
                x1, y1,   // northEast
                x1, y2,   // southEast
                x2, y2);

        Query query = entityManager.createNativeQuery(
                "SELECT * \n" +
                        "FROM archives AS a \n" +
                        "WHERE MBRContains(ST_POLYGONFROMTEXT('" + polygonWKT + "'), a.location)" +
                        "AND a.is_public = true"
                , Archive.class
        );

        return (List<Archive>) query.getResultList();
    }
}
