package com.popcorntalk.domain.user.service;

import static com.popcorntalk.global.exception.ErrorCode.DUPLICATE_USER;

import com.popcorntalk.domain.user.dto.SignupRequestDto;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.exception.customException.DuplicateUserInfoException;
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

    public void signup(SignupRequestDto signupRequestDto) {

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateUserInfoException(DUPLICATE_USER);
        }

        User user = User.SignupOf(
            signupRequestDto.getEmail(),
            passwordEncoder.encode(signupRequestDto.getPassword()));

        userRepository.save(user);
    }
}

