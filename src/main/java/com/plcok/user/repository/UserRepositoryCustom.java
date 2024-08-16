package com.plcok.user.repository;

import com.plcok.user.dto.UserRetrieveRequest;
import com.plcok.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> retrieve(User user, UserRetrieveRequest request);

    int count(User user, UserRetrieveRequest request);
}
