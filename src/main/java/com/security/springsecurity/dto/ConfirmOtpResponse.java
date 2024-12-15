package com.security.springsecurity.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOtpResponse {
     private String otp;
    private String username;
    private int userId;
    private int result;
    private String errorMessage;
}
