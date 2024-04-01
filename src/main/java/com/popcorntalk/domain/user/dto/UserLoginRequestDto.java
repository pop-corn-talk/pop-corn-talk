package com.popcorntalk.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
  @NotBlank(message = "아이디를 입력하세요.")
  private String email;
  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;
}
