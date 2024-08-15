package com.plcok.common.service;

import com.plcok.archive.repository.ReportRepository;
import com.plcok.common.BaseEntity;
import com.plcok.common.entity.Report;
import com.plcok.user.entity.User;
import com.plcok.common.entity.IReportable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public <T extends BaseEntity & IReportable> void report(User user, T resource) {
        reportRepository.save(
                Report.builder()
                        .author(user)
                        .user(resource.getUser())
                        .resourceId(resource.getId())
                        .resourceType(resource.getResourceType())
                        .build()
        );
    }
}
