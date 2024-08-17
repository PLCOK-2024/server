package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.QArchive;
import com.plcok.user.entity.QBlock;
import com.plcok.user.entity.User;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ArchiveRepositoryImpl implements ArchiveRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Archive> findNearArchives(User user, double y1, double x1, double y2, double x2) {
        var query = queryFactory.select(QArchive.archive).from(QArchive.archive);

        if (true && user != null) { // 차단 한거 미노출 할지
            var subQuery = JPAExpressions.selectFrom(QBlock.block1)
                    .where(QBlock.block1.author.eq(user))
                    .where(QBlock.block1.block.eq(QArchive.archive.author));

            query.where(subQuery.notExists());
        }

        if (true) { // 위치 검색인지
            String polygonWKT = String.format("ST_POLYGONFROMTEXT('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))')",
                    x2, y2,   // southWest
                    x2, y1,   // northWest
                    x1, y1,   // northEast
                    x1, y2,   // southEast
                    x2, y2);

            query.where(
                    Expressions.booleanTemplate(
                            "MBRContains(" + polygonWKT + ", {0})",
                            QArchive.archive.location
                    ).isTrue()
            );
        }

        return query.fetch();
    }
}
