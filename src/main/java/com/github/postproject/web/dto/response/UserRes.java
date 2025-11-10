package com.github.postproject.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {
    private Integer id;
    private String email;
    private String role;
}
