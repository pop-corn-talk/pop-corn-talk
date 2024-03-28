package com.popcorntalk.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형태로 입력해 주세요.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "비밀번호는 알파벳과 숫자로 구성할 수 있습니다.")
    private String password;
}
