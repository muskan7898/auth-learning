package com.example.Authentication.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SignupResponse {
    private Long id;
    private String username;
    private String password;
}
