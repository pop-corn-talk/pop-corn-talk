package com.popcorntalk.domain.user.service;

import static com.popcorntalk.global.exception.ErrorCode.DUPLICATE_USER;

import com.popcorntalk.domain.point.service.PointServiceImpl;
import com.popcorntalk.domain.post.service.PostRecodeServiceImpl;
import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.ErrorCode;
import com.popcorntalk.global.exception.customException.DuplicateUserInfoException;
import com.popcorntalk.global.exception.customException.UserNotFoundException;
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
  private final PointServiceImpl pointService;
  private final PostRecodeServiceImpl postRecodeService;

  @Override
  public void signup(UserSignupRequestDto userSignupRequestDto) {

    if (userRepository.existsByEmail(userSignupRequestDto.getEmail())) {
      throw new DuplicateUserInfoException(DUPLICATE_USER);
    }
    User user = User.createOf(
        userSignupRequestDto.getEmail(),
        passwordEncoder.encode(userSignupRequestDto.getPassword()),
        DeletionStatus.N,
        UserRoleEnum.USER);

    pointService.rewardPointForSignUp(userRepository.save(user).getId());
  }

  @Override
  @Transactional(readOnly = true)
  public UserInfoResponseDto getMyInfo(Long userId) {

    User user = userRepository.getUser(userId);

    int dailyPostCount = postRecodeService.getPostCountInToday(userId);
    if(dailyPostCount<3){
      dailyPostCount = 3-dailyPostCount;
    }
    else {
      dailyPostCount = 0;
    }

    return new UserInfoResponseDto(user.getEmail(),pointService.getPoint(userId).getPoint(),dailyPostCount);
  }

  @Override
  public UserPublicInfoResponseDto getUserInfo(Long userId) {

    return userRepository.getUserEmail(userId);
  }

  @Override
  public Page<UserPublicInfoResponseDto> getAllUserInfo(Pageable pageable) {

    return userRepository.getPageUsers(pageable);
  }

  @Override
  public void validateAdminUser(Long id) {

    if(!userRepository.validateAdminUser(id)){
      // todo 승현님이 만들어 놓운 exception 으로 변경
      throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
    }
  }
}
