package com.security.springsecurity.dto;

import com.security.springsecurity.entity.Permission;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Role {
    String name;
    @OneToMany
    private List<Permission> permissionList;
}
