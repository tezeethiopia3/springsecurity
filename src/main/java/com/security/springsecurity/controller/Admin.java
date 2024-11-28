package com.security.springsecurity.controller;

import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.dto.PermissionDto;
import com.security.springsecurity.dto.ResourceName;
import com.security.springsecurity.dto.Role;
import com.security.springsecurity.dto.RoleName;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequestMapping("admin")
@RestController
@AllArgsConstructor
public class Admin {
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;
    private final AuthAccessListRepository authAccessListRepository;

    @PostMapping("/createRole")
//    public AuthRole CreateRole(@RequestBody Role role)
    public AuthRole CreateRole(@RequestBody AuthRole role)
    {
        System.out.println("CreateRole method==============");
//        System.out.println("CreateRole method==============+role.getName()"+role.getName());
//        System.out.println("CreateRole method==============+role.getAuthAccessList().get(0).getName()"+role.getAccessLists().get(0).getName());
        role.setCreatedDate(LocalDateTime.now());
        return  authenticationService.createRole(role);

    }
    @PostMapping("/updateRole")
    public Optional<AuthRole> updateRole(@RequestBody AuthRole authRole)
    {
        System.out.println("CreateRole method==============");
        return authenticationService.updateRole(authRole);
    }
    @PostMapping("/createPermission")
    public AuthAccessList createPermission(@RequestBody AuthAccessList permissionDto)
    {
        return authenticationService.createPermssion(permissionDto);
    }
}
