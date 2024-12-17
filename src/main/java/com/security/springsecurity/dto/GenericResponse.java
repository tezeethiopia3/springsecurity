package com.security.springsecurity.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    @Email(message="Email format is not correct")
    private String email;
    private int result;
    private String errorMessage;
    private int userId;
}
