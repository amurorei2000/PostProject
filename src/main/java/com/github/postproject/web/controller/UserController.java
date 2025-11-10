package com.github.postproject.web.controller;

import com.github.postproject.service.UserService;
import com.github.postproject.web.dto.request.UserReq;
import com.github.postproject.web.dto.response.UserRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserRes> signIn(@RequestBody UserReq userReq) {
        UserRes user = userService.signIn(userReq);
        log.info("create user: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserRes> login(@RequestBody UserReq userReq) {
        UserRes user = userService.login(userReq);
        log.info("login user: {}", user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
