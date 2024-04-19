package com.popcorntalk.global.log.controller;

import com.popcorntalk.global.log.service.TestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogController {
    private final TestLogService logService;

    @GetMapping
    public ResponseEntity<Void> getLogs() {
        logService.getLogs();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
