package com.security.springsecurity.controller;

import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.dto.*;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthToken;
import com.security.springsecurity.entity.AuthUser;
import com.security.springsecurity.service.AuthenticationService;
import com.security.springsecurity.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("admin")
@RestController
@AllArgsConstructor
public class Admin {
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;
    private final AuthAccessListRepository authAccessListRepository;

    private final  EmailService emailService;

    @PostMapping("/sendMail")
    public void sendMail(@RequestBody EmailProperties emailProperties) throws MessagingException {
        emailService.sendMail(emailProperties);

    }

    @PostMapping("/updateRole")
    public Optional<AuthRole> updateRole(@RequestBody AuthRole authRole)
    {
        System.out.println("updateRole method==============");
        return authenticationService.updateRole(authRole);
    }
//    @PostMapping("/createPermission")
//    public AuthAccessList createPermission(@RequestBody AuthAccessList permissionDto)
//    {
//        return authenticationService.createPermssion(permissionDto);
//    }
    @PostMapping("/saveResource")
    public AuthAccessList saveResource(@RequestBody AuthAccessList authAccessList)
    {

       return authenticationService.saveResource(authAccessList);

    }
    @PostMapping("/saveRole")
    public AuthRole saveRole(@RequestBody AuthRole authRole)
    {
        System.out.println("saveRole=================");
        return authenticationService.saveRole(authRole) ;
    }
    @PostMapping("/grantRoleToUser")
    public Optional<AuthUser> grantRoleToUser(@ RequestBody RoleGrantToUserRequest roleGrantToUserRequest){
        System.out.println("grantRoleToUser=============");
        return  authenticationService.grantRoleToUser(roleGrantToUserRequest);
    }
    @PostMapping("/passwordChange")
    public GenericResponse passwordChange(@RequestBody ChangePasswordRequest changePasswordRequest)
    {
        System.out.println("passwordChange method=========");
      return  authenticationService.passwordChange(changePasswordRequest);

    }

@GetMapping("/getAllAccessByRole")
    public List<AuthAccessList> getAllAccessByUserRole()
    {
        System.out.println("controller getAllAccessByUserRole=========");

        return authenticationService.getAllAccessByUserRole();

    }

    @GetMapping("/getAllAccessByRoleName")
    public List<AuthAccessList> getAllAccessByUserRole(@RequestParam String rolename)
    {
        System.out.println("inside Controller getAllAccessByUserRole=="+rolename);

        return authenticationService.getAllAccessByUserRoleNeme(rolename);

    }
    @PostMapping("/changePassword")

    public GenericResponse ChangePassword(@RequestBody  ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
      return  authenticationService.ChangePassword(changePasswordRequest,connectedUser);
    }
    @PutMapping("/grantAccessToRole")
    public GenericResponse assignAccessToRole(@RequestBody AuthRole authRole)
    {

        return  authenticationService.assignAccessToRole(authRole);

    }



}
