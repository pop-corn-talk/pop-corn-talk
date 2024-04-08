package com.popcorntalk.domain.usertest;


import com.popcorntalk.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

}
