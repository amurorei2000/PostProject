package com.github.postproject.web.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentRes {
    Integer id;
    String content;
    String writer;
    Integer postId;
    String postedAt;
}
