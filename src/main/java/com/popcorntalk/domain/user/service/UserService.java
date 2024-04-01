package com.popcorntalk.domain.user.service;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    /**
     * 회원가입
     *
     * @param userSignupRequestDto 생성될 유저의 내용
     *
     */
    void signup(UserSignupRequestDto userSignupRequestDto);


    /**
     * 개인(본인) 정보 조회
     *
     * @param userId 조회할 유저의 내용
     *
     */
    UserInfoResponseDto getMyInfo(Long userId);

    /**
     * 개인(타인) 정보 조회
     *
     * @param userId 조회할 유저의 내용
     *
     */
    UserPublicInfoResponseDto getUserInfo(Long userId);

    /**
     * 개인(타인) 정보 페이지 로 조회
     *
     * @param pageable 조회할 페이지 요청
     *
     */
    Page<UserPublicInfoResponseDto> getAllUserInfo(Pageable pageable);
}
