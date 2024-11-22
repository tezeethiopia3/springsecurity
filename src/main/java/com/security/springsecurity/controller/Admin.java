package com.security.springsecurity.controller;

import com.security.springsecurity.dto.PermissionDto;
import com.security.springsecurity.dto.Role;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.Permission;
import com.security.springsecurity.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("admin")
@RestController
@AllArgsConstructor
public class Admin {
    private final AuthenticationService authenticationService;
    @PostMapping("/createrole")
    public ResponseEntity<?> CreateRole(@RequestBody Role role)
    {
        authenticationService.createRole(role);
        return ResponseEntity.accepted().build();

    }
    @PostMapping("/createpermission")
    public ResponseEntity<String> createPermission(@RequestBody PermissionDto permissionDto)
    {
        Permission permission=new Permission();
        permission.setName(permissionDto.getName());
        AuthRole authRole=new AuthRole();
        System.out.println("permissionDto.getRoleId()=="+permissionDto.getRoleId());
        authRole.setId(permissionDto.getRoleId());
        permission.setRole(authRole);
        authenticationService.createPermssion(permission);
        return ResponseEntity.ok("Permission Created Successfully");
    }
}
