package com.plcok.user.service.relationship;

import com.plcok.user.entity.User;

public interface IUserRelationshipService {
    void link(User source, User target);
    int unlink(User source, User target);
}
