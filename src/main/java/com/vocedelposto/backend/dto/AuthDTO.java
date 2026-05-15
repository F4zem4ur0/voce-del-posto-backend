package com.vocedelposto.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {

    // Request
    private String email;
    private String password;

    // Response
    private String token;
    private Long userId;
    private String username;
}