package com.security.springsecurity.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private String name;
    private boolean status;
}
