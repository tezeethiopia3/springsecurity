package com.security.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordRequestUtil {
    private String userEmail;
    private String oldPassword;
    private String newPassword;
}
