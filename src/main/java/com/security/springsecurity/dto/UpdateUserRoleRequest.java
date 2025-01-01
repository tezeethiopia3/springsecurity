package com.security.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {
    private String email;
    private String newRole;
    private String oldRole;
    private String role;
    private String nametwo;

}
