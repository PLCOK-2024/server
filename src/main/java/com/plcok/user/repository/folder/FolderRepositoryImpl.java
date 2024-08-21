package com.plcok.user.repository.folder;

import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.QFolder;
import com.plcok.user.entity.QFolderArchive;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.plcok.user.entity.QFolder.folder;
import static com.plcok.user.entity.QFolderArchive.folderArchive;

@RequiredArgsConstructor
public class FolderRepositoryImpl implements FolderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FolderResponse> listFolderByUserId(long userId) {
        return queryFactory
                .select(Projections
                        .constructor(FolderResponse.class,
                            folder.id.as("id"),
                            folder.name,
                            folderArchive.count().as("count")
                        )
                )
                .from(folder)
                .leftJoin(folder.folderArchives, folderArchive)
                .where(folder.user.id.eq(userId))
                .groupBy(folder.id)
                .fetch();
    }
}
