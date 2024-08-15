package com.example.demo.user.service.relationship;

import com.example.demo.user.domain.User;

public interface IUserRelationshipService {
    void link(User source, User target);
    int unlink(User source, User target);
}
