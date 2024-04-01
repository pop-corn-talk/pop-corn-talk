package com.popcorntalk.domain.user.service;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    //회원가입
    void signup(UserSignupRequestDto userSignupRequestDto);

    // 유저가 자신 개인의 정보를 조회. email 포인트 포스트 횟수
    UserInfoResponseDto getUserInfo(Long userId);

    // 유저가 다른 이의 정보를 확인. email 만
    UserPublicInfoResponseDto getOtherUserInfo(Long userId);

    // 유저가 다른 이들의 정보를 확인. email 만
    Page<UserPublicInfoResponseDto> getAllOtherUserInfo(Pageable pageable);
}
