package com.plcok.common.entity;

import com.plcok.common.entity.enumerated.ResourceType;
import com.plcok.user.entity.User;

public interface IReportable {
    User getUser();
    ResourceType getResourceType();
}
