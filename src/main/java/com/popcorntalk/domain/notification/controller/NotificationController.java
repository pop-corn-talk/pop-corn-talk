package com.popcorntalk.domain.notification.controller;

import com.popcorntalk.domain.notification.service.NotificationService;
import com.popcorntalk.global.security.UserDetailsImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/notification/subscribe")
    public SseEmitter subscribe(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        SseEmitter sseEmitter = notificationService.subscribe(userDetails.getUser().getId());
        return sseEmitter;
    }
}
