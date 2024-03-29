package com.popcorntalk.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserPublicInfoResponseDto {

  private String email;
  public UserPublicInfoResponseDto(String email) {
    this.email = email;
  }
}
