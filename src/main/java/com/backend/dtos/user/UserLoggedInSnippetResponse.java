package com.backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserLoggedInSnippetResponse {
    private String username;
    private String userDp;
}
