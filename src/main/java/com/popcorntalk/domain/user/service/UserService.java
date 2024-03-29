package com.popcorntalk.domain.user.service;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void signup(UserSignupRequestDto userSignupRequestDto);

    UserInfoResponseDto getUserInfo(Long userId);

    UserPublicInfoResponseDto getOtherUserInfo(Long userId);

    Page<UserPublicInfoResponseDto> getAllOtherUserInfo(Pageable pageable);
}
