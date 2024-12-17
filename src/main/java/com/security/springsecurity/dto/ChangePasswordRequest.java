package com.security.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private  String username;
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;
}
