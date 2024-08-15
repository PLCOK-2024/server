package com.example.demo.common.entity;

import com.example.demo.common.entity.enumerated.ResourceType;
import com.example.demo.user.domain.User;

public interface IReportable {
    User getUser();
    ResourceType getResourceType();
}
