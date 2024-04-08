package com.popcorntalk.domain.usertest;

// ... (중략) ...

import com.popcorntalk.domain.user.dto.UserSignupRequestDto;
import com.popcorntalk.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserTestController {

    private final UserService userService;
    @GetMapping("/users/signup")
    public String getSignupPage() {
        return "user/signup";
    }
    @GetMapping("/users/testLogin")
    public String showLoginPage() {
        return "user/login"; // Assuming "login" is the name of your login page template
    }

    @PostMapping("/users/signup")
    public ResponseEntity<UserSignupRequestDto> createSignup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto,
        BindingResult bindingResult, Model model) {


        userService.signup(userSignupRequestDto);
    }
}
