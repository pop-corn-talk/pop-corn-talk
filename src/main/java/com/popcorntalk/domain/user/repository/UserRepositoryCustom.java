package com.popcorntalk.domain.user.repository;

import com.popcorntalk.domain.user.entity.User;

public interface UserRepositoryCustom {

    User getUser(Long userId);

}
