package com.popcorntalk.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.service.PointServiceImpl;
import com.popcorntalk.domain.post.service.PostRecodeServiceImpl;
import com.popcorntalk.domain.user.dto.UserInfoResponseDto;
import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.domain.user.service.UserServiceImpl;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock
  UserRepository mockRepo;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  PointServiceImpl pointService;

  @Mock
  PostRecodeServiceImpl postRecodeService;

  /*
   *
   * 관지자 검증 관련.
   *
   */

  @Test
  @DisplayName("관리자 유저일 경우")
  void validateAdminSuccess(){

    //given
    long userId = 1;
    boolean flag = true;

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService,postRecodeService);

    given(mockRepo.validateAdminUser(userId)).willReturn(true);

    //when
    try {
      userService.validateAdminUser(userId);
    }catch (NotFoundException e){
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

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService,postRecodeService);

    given(mockRepo.validateAdminUser(userId)).willReturn(false);

    //when
    try {
      userService.validateAdminUser(userId);
    }catch (NotFoundException e){
      flag = false;
    }

    // then
    assertFalse("해당 유저는 관리자가 아니에요",flag);
  }

  /*
   *
   * 유저 정보 가져조기 관련
   *
   */

  @Test
  @DisplayName("개인 유저가 자신의 정보 보기")
  void getMyInfo(){

    //given
    long userId = 1;
    User user = User.builder()
        .id(userId)
        .email("email1@email.com")
        .password("1231234123")
        .deletionStatus(DeletionStatus.N)
        .role(UserRoleEnum.USER)
        .build();

    Point point = Point.createOf(userId,100);

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService,postRecodeService);


    given(mockRepo.getUser(userId)).willReturn(user);
    when(pointService.getPoint(userId)).thenReturn(point);
    when(postRecodeService.getPostCountInToday(userId)).thenReturn(2);
    //when

    UserInfoResponseDto userInfoResponseDto = userService.getMyInfo(userId);

    // then
    assertEquals("개인 유저가 자신의 이메일 보기 ",user.getEmail(),userInfoResponseDto.getEmail());
    assertEquals("개인 유저가 자신의 포인트 보기 ",100,userInfoResponseDto.getPoint());
    assertEquals("개인 유저가 자신의 일일 횟수 보기 2번 포스트 했기에 갑은 1",1,userInfoResponseDto.getDailyPostsLimit());
  }

  @Test
  @DisplayName("개인 유저가 타인의 정보 보기 (개인)")
  void getUserInfo(){

    //given
    long userId = 2;
    UserPublicInfoResponseDto userPublicInfoResponseDto = UserPublicInfoResponseDto.builder()
        .email("email1@email.com")
        .build();

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService,postRecodeService);

    given(mockRepo.getUserEmail(userId)).willReturn(userPublicInfoResponseDto);

    // when
    UserPublicInfoResponseDto userPublicInfoResponseDto1 = userService.getUserInfo(userId);

    // then
    assertEquals("개인 유저가 자신의 이메일 보기 ",userPublicInfoResponseDto1.getEmail(),userPublicInfoResponseDto.getEmail());
  }

  @Test
  @DisplayName("개인 유저가 타인의 정보 보기 (단체/페이지)")
  void getAllUserInfo(){

    //given
    UserPublicInfoResponseDto user = UserPublicInfoResponseDto.builder()
        .email("email1@email.com")
        .build();

    //when
    Pageable pageable = Pageable.ofSize(10).withPage(0); // Assuming page size is 10 and page number is 0
    List<UserPublicInfoResponseDto> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user);
    userList.add(user);

    Page<UserPublicInfoResponseDto> userPage = new PageImpl<>(userList, pageable, userList.size());

    UserServiceImpl userService = new UserServiceImpl(mockRepo,passwordEncoder,pointService,postRecodeService);
    when(mockRepo.getPageUsers(pageable)).thenReturn(userPage);


    Page<UserPublicInfoResponseDto> result = userService.getAllUserInfo(pageable);

    // then
    assertEquals("유저 element 갯수",userPage.getTotalElements(), result.getTotalElements());
    assertEquals("유저 페이지 총 갯수",userPage.getTotalPages(), result.getTotalPages());
    assertEquals("유저 페이지 갯수",userPage.getNumber(), result.getNumber());
    assertEquals("유저 페이지 크기",userPage.getSize(), result.getSize());
    assertEquals("유저 컨텐트 크기",userPage.getContent().size(), result.getContent().size());

  }
}

