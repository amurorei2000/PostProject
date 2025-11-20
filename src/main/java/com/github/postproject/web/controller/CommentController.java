package com.github.postproject.web.controller;

import com.github.postproject.service.CommentService;
import com.github.postproject.web.dto.request.CommentReq;
import com.github.postproject.web.dto.response.CommentRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentRes> createComment(@PathVariable int postId, @RequestBody CommentReq commentReq) {

        log.info(commentReq.toString());
        CommentRes result = commentService.createComment(postId, commentReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentRes> updateComment(@PathVariable int commentId, @RequestBody CommentReq commentReq) {
//        log.info("id: {}", commentId);
        CommentRes result = commentService.updateComment(commentId, commentReq);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        boolean result = commentService.deleteComment(commentId);

        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 삭제에 실패하였습니다.");
        }

    }
}
