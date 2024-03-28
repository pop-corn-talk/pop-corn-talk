package com.popcorntalk.domain.user.service;

import com.popcorntalk.domain.user.dto.SignupRequestDto;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.util.JwtUtil;
import com.popcorntalk.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    public void signup(SignupRequestDto requestDto) {

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("error");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
            .email(requestDto.getEmail())
            .password(password)
            .role(UserRoleEnum.USER)
            .build();

        userRepository.save(user);
    }

    public void logout(Long userId, String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("error");
        }
        jwtUtil.deleteRefreshToken(userId);
        Long expiration = jwtUtil.getExpiration(token);
        redisUtil.setBlackList(token, userId, expiration);
    }
}

