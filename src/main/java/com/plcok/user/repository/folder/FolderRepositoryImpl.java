package com.plcok.user.repository.folder;

import com.plcok.user.dto.response.FolderResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.plcok.archive.entity.QArchive.archive;
import static com.plcok.user.entity.QFolder.folder;

@RequiredArgsConstructor
public class FolderRepositoryImpl implements FolderRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<FolderResponse> listFolderByUserId(long userId) {
        return queryFactory
                .select(Projections.constructor(FolderResponse.class,
                        folder.id,
                        folder.name,
                        folder.count)
                )
                .from(folder)
                .where(folder.user.id.eq(userId))
                .fetch();
    }
}
