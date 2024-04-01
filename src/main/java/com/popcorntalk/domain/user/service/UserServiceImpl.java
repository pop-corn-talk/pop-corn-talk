package com.popcorntalk.domain.user.service;

import static com.popcorntalk.global.exception.ErrorCode.DUPLICATE_USER;

import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.exception.customException.DuplicateUserInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void signup(UserSignupRequestDto userSignupRequestDto) {

    if (userRepository.existsByEmail(userSignupRequestDto.getEmail())) {
      throw new DuplicateUserInfoException(DUPLICATE_USER);
    }
    User user = User.SignupOf(
        userSignupRequestDto.getEmail(),
        passwordEncoder.encode(userSignupRequestDto.getPassword()));

    userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoResponseDto getMyInfo(Long userId) {

    User user = userRepository.getUser(userId);

    return UserInfoResponseDto.builder()
        .email(user.getEmail())
        // todo point 랑 maxDailyPostsLimit 랑 저장 하는법 다시 필요합니다. Point 테이플 이 생기면 업데이트 하겠습니다.
//        .point(user.getPoint())
//        .maxDailyPostsLimit(user.getMaxDailyPostsLimit())
        .build();
  }

  @Override
  public UserPublicInfoResponseDto getUserInfo(Long userId) {

    return userRepository.getUserEmail(userId);
  }

  @Override
  public Page<UserPublicInfoResponseDto> getAllUserInfo(Pageable pageable) {

    return userRepository.getPageUsers(pageable);
  }
}
