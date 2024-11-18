package com.security.springsecurity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "firstName can not be empty")
    @NotBlank(message = "firstName can not be blank")
    private String firstName;
    @NotEmpty(message = "lastName can not be empty")
    @NotBlank(message = "lastName can not be blank")
    private String lastName;
    @NotEmpty(message = "email can not be empty")
    @NotBlank(message = "email can not be blank")
    @Email(message="Email format is not correct")
    private String email;
    @NotEmpty(message = "password can not be empty")
    @NotBlank(message = "password can not be blank")
    @Size(min = 8,message = "Minimum length should be 8")
    private String password;
}
