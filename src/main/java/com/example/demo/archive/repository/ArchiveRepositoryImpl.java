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
    public List<Archive> findNearArchives(double currentX, double currentY) {
        Pair<Double, Double> northEast = RadiusCalculator.calculateByDirection(currentX, currentY, DISTANCE, CardinalDirection.NORTHEAST.getBearing());
        Pair<Double, Double> southWest = RadiusCalculator.calculateByDirection(currentX, currentY, DISTANCE, CardinalDirection.SOUTHWEST.getBearing());

        double x1 = northEast.getSecond();
        double y1 = northEast.getFirst();
        double x2 = southWest.getSecond();
        double y2 = southWest.getFirst();

        Query query = entityManager.createNativeQuery("" +
                        "SELECT * \n" +
                        "FROM archives AS a \n" +
                        "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2) + ", a.location)"
        );
        return query.getResultList();
    }
}
