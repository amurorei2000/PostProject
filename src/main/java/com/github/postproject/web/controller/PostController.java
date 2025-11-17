package com.github.postproject.web.controller;

import com.github.postproject.repository.post.Posts;
import com.github.postproject.service.PostService;
import com.github.postproject.web.dto.request.PostReq;
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

    @Operation(summary = "게시글 생성")
    @PostMapping("/createPost")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostRes> createPost(@RequestBody PostReq postReq) {
        Posts createdPost = postService.createPost(postReq);
        PostRes response = PostRes.builder()
                .title(createdPost.getTitle())
                .content(createdPost.getContent())
                .writerId(createdPost.getUsers().getId())
                .authorName(createdPost.getWriterName())
                .viewCount(createdPost.getViewCnt())
                .likeCount(createdPost.getLikeCnt())
                .createdAt(createdPost.getCreatedAt())
                .updatedAt(createdPost.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 수정")
    @PostMapping("/updatePost/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostRes> updatePost(@PathVariable Integer postId, @RequestBody PostReq postReq) {
        Posts updatedPost = postService.updatePost(postId, postReq);
        PostRes response = PostRes.builder()
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .writerId(updatedPost.getUsers().getId())
                .authorName(updatedPost.getWriterName())
                .viewCount(updatedPost.getViewCnt())
                .likeCount(updatedPost.getLikeCnt())
                .createdAt(updatedPost.getCreatedAt())
                .updatedAt(updatedPost.getUpdatedAt())
                .build();

        log.info("게시물 수정: {}", postId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/deletePost/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        boolean result = postService.deletePost(postId);

        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("게시물 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 삭제 실패");
        }
    }
}
