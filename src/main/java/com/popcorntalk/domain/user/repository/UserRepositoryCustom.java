package com.popcorntalk.domain.user.repository;

import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

  User getUser(Long userId);

  Page<UserPublicInfoResponseDto> getPageUsers(Pageable pageable);
}