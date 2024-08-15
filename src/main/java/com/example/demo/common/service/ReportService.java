package com.example.demo.common.service;

import com.example.demo.archive.repository.ReportRepository;
import com.example.demo.common.BaseEntity;
import com.example.demo.common.entity.IReportable;
import com.example.demo.common.entity.Report;
import com.example.demo.user.domain.User;
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
