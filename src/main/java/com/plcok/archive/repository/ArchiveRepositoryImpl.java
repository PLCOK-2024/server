package com.plcok.archive.repository;

import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.QArchive;
import com.plcok.archive.entity.QArchiveTag;
import com.plcok.user.entity.QBlock;
import com.plcok.user.entity.QFollower;
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
    public List<Archive> retrieve(User user, ArchiveRetrieveRequest request) {
        var query = queryFactory.select(QArchive.archive).from(QArchive.archive);

        // 위치 검색
        if (
                request.getTopLeftLatitude() != null &&
                request.getTopLeftLongitude() != null &&
                request.getBottomRightLatitude() != null &&
                request.getBottomRightLongitude() != null
        ) {
            String polygonWKT = String.format("ST_POLYGONFROMTEXT('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))')",
                    request.getBottomRightLongitude(), request.getBottomRightLatitude(),   // southWest
                    request.getBottomRightLongitude(), request.getTopLeftLatitude(),   // northWest
                    request.getTopLeftLongitude(), request.getTopLeftLatitude(),   // northEast
                    request.getTopLeftLongitude(), request.getBottomRightLatitude(),   // southEast
                    request.getBottomRightLongitude(), request.getBottomRightLatitude());

            query.where(
                    Expressions.booleanTemplate(
                            "MBRContains(" + polygonWKT + ", {0})",
                            QArchive.archive.location
                    ).isTrue()
            );
        }

        // 작성자 차단
        if (request.getBlock() != null && user != null) {
            var subQuery = JPAExpressions.selectFrom(QBlock.block1)
                    .where(QBlock.block1.author.eq(user))
                    .where(QBlock.block1.block.eq(QArchive.archive.author));

            query.where(request.getBlock() ? subQuery.exists() : subQuery.notExists());
        }

        // 팔로우
        if (request.getFollow() != null && user != null) {
            var subQuery = JPAExpressions.selectFrom(QFollower.follower1)
                    .where(QFollower.follower1.follower.eq(user))
                    .where(QFollower.follower1.follow.eq(QArchive.archive.author));

            query.where(
                    request.getFollow() ? subQuery.exists() : subQuery.notExists()
            );
        }

        // 태그 검색
        if (request.getTag() != null) {
            var subQuery = JPAExpressions.selectFrom(QArchiveTag.archiveTag)
                    .where(QArchiveTag.archiveTag.archive.eq(QArchive.archive))
                    .where(QArchiveTag.archiveTag.name.eq(request.getTag()));

            query.where(subQuery.exists());
        }

        return query.orderBy(QArchive.archive.createdAt.desc()).fetch();
    }
}
