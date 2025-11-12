package com.github.postproject.web.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostRes {
    private String title;
    private String content;
    private Integer writerId;
    private Integer viewCount;
    private Integer likeCount;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
