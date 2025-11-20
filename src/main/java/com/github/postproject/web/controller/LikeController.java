package com.github.postproject.web.controller;

import com.github.postproject.service.LikeService;
import com.github.postproject.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가/삭제")
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Boolean>> toggleLike(@PathVariable int postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userEmail =  userDetails.getUsername();

        boolean result = likeService.toggleLike(postId, userEmail);
        Map<String, Boolean> response = new HashMap<>();
        response.put("like", result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
