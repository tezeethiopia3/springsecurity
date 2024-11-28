package com.security.springsecurity.dto;

import com.security.springsecurity.entity.AuthRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PermissionDto {
    private String name;
    private List<RoleName> roleNames;
}
