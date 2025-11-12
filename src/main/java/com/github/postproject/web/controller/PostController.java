package com.github.postproject.web.controller;

import com.github.postproject.repository.post.Posts;
import com.github.postproject.service.PostService;
import com.github.postproject.web.dto.response.PostRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @Operation(summary = "모든 게시글 가져오기")
    @GetMapping("/viewAllPosts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostRes>> viewAllPosts() {
        List<Posts> results = postService.viewAllPosts();
        List<PostRes> postResponses = results.stream()
                .map(result -> PostRes.builder()
                        .title(result.getTitle())
                        .content(result.getContent())
                        .writerId(result.getUsers().getId())
                        .authorName(result.getWriterName())
                        .viewCount(result.getViewCnt())
                        .likeCount(result.getLikeCnt())
                        .createdAt(result.getCreatedAt())
                        .updatedAt(result.getUpdatedAt())
                        .build()
                )
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }

    @Operation(summary = "이메일로 게시글 가져오기")
    @GetMapping("/viewPostsByEmail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostRes>> viewPostsByEmail(@RequestParam(name = "email") String email) {
        List<Posts> results = postService.viewPostsByEmail(email);
        List<PostRes> postResponses = results.stream()
                .map(result -> PostRes.builder()
                        .title(result.getTitle())
                        .content(result.getContent())
                        .writerId(result.getUsers().getId())
                        .authorName(result.getWriterName())
                        .viewCount(result.getViewCnt())
                        .likeCount(result.getLikeCnt())
                        .createdAt(result.getCreatedAt())
                        .updatedAt(result.getUpdatedAt())
                        .build()
                )
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }
}
