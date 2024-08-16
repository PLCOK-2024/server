package com.plcok.user.repository;

import com.plcok.user.dto.UserRetrieveRequest;
import com.plcok.user.entity.QBlock;
import com.plcok.user.entity.QFollower;
import com.plcok.user.entity.QUser;
import com.plcok.user.entity.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private JPAQuery<?> retrieveQuery(User user, UserRetrieveRequest request) {
        var query = queryFactory.from(QUser.user);

        if (request.getQ() != null) {
            query.where(QUser.user.name.contains(request.getQ()));
        }

        if (user != null) {
            query.where(QUser.user.ne(user));
        }

        if (user != null && request.getBlock() != null) {
            var subQuery = JPAExpressions.selectFrom(QBlock.block1)
                    .where(QBlock.block1.author.eq(user))
                    .where(QBlock.block1.block.eq(QUser.user));

            query.where(
                    request.getBlock() ? subQuery.exists() : subQuery.notExists()
            );
        }

        if (user != null && request.getFollow() != null) {
            var subQuery = JPAExpressions.selectFrom(QFollower.follower1)
                    .where(QFollower.follower1.follower.eq(user))
                    .where(QFollower.follower1.follow.eq(QUser.user));

            query.where(
                    request.getFollow() ? subQuery.exists() : subQuery.notExists()
            );
        }

        if (user != null && request.getFollower() != null) {
            var subQuery = JPAExpressions.selectFrom(QFollower.follower1)
                    .where(QFollower.follower1.follow.eq(user))
                    .where(QFollower.follower1.follower.eq(QUser.user));

            query.where(
                    request.getFollower() ? subQuery.exists() : subQuery.notExists()
            );
        }

        return query;
    }

    @Override
    public List<User> retrieve(User user, UserRetrieveRequest request) {
        return retrieveQuery(user, request)
                .select(QUser.user)
                .limit(request.getLimit())
                .fetch();
    }

    @Override
    public int count(User user, UserRetrieveRequest request) {
        var result = retrieveQuery(user, request)
                .select(QUser.user.count())
                .fetchFirst();

        return result == null ? 0 : result.intValue();
    }
}