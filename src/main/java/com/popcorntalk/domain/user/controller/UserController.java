package com.popcorntalk.domain.user.controller;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
        @Valid @RequestBody UserSignupRequestDto userSignupRequestDto
    ) {
        userService.signup(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info")
    public ResponseEntity<CommonResponseDto<UserInfoResponseDto>> getMyInfo(
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return ResponseEntity.ok().body(
            CommonResponseDto.success(userService.getMyInfo(userDetailsImpl.getUser().getId())));
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<CommonResponseDto<UserPublicInfoResponseDto>> getUserInfo(
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok().body(
            CommonResponseDto.success(userService.getUserInfo(userId)));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<Page<UserPublicInfoResponseDto>>> getAllUserInfo(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok().body(
            CommonResponseDto.success(userService.getAllUserInfo(pageable)));
    }
}

