package com.popcorntalk.domain.user.service;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import com.popcorntalk.domain.user.entity.User;

public interface UserService {

    void signup(UserSignupRequestDto userSignupRequestDto);

    UserInfoResponseDto getUserInfo(Long userId);
}
