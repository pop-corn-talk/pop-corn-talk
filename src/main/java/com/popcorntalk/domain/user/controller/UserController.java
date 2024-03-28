package com.popcorntalk.domain.user.controller;

import com.popcorntalk.domain.user.dto.SignupRequestDto;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<Void> signup(
        @Valid @RequestBody SignupRequestDto requestDto
    ) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/users/logout")
    public ResponseEntity<CommonResponseDto<Void>> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        HttpServletRequest req
    ) {
        String bearerToken = req.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        userService.logout(userDetails.getUser().getId(), bearerToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

