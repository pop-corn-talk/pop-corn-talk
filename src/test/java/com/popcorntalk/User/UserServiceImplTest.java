package com.popcorntalk.User;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.popcorntalk.domain.point.service.PointServiceImpl;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.domain.user.service.UserServiceImpl;
import com.popcorntalk.global.exception.customException.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock
  UserRepository mockRepo;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  PointServiceImpl pointService;

  @Test
  @DisplayName("관리자 유저일 경우")
  void validateAdminSuccess(){

    //given
    long userId = 1;
    boolean flag = true;

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService);

    given(mockRepo.validateAdminUser(userId)).willReturn(true);

    //when
    try {
      userService.validateAdminUser(userId);
    }catch (UserNotFoundException e){
      flag = false;
    }

    // then
    assertTrue("성공 해당 유저는 관리자가 맞아요",flag);
  }

  @Test
  @DisplayName("관리자 유저가 아닐 경우")
  void validateAdminFail(){

    //given
    long userId = 1;
    boolean flag = true;

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService);

    given(mockRepo.validateAdminUser(userId)).willReturn(false);

    //when
    try {
      userService.validateAdminUser(userId);
    }catch (UserNotFoundException e){
      flag = false;
    }

    // then
    assertFalse("해당 유저는 관리자가 아니에요",flag);
  }
}

