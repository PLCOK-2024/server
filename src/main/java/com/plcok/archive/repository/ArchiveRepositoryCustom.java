package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.user.entity.User;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> findNearArchives(User author, double topLeftLatitude, double topLeftLongitude, double bottomRightLatitude, double bottomRightLongitude);
}
