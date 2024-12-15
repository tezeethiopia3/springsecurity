package com.security.springsecurity.controller;

import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.dto.EmailProperties;
import com.security.springsecurity.dto.PasswordRequestUtil;
import com.security.springsecurity.dto.UserDto;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthUser;
import com.security.springsecurity.service.AuthenticationService;
import com.security.springsecurity.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Optional<AuthUser> grantRoleToUser(@ RequestBody AuthUser user){
        System.out.println("grantRoleToUser=============");
        return  authenticationService.grantRoleToUser(user);
    }
    @PostMapping("/passwordChange")
    public boolean passwordChange(@RequestBody PasswordRequestUtil passwordRequestUtil)
    {
        System.out.println("passwordChange method=========");
      return  authenticationService.passwordChange(passwordRequestUtil);

    }
@RequestMapping("/getAllUser")
    public List<UserDto> getAllUser(){
        return authenticationService.getAllUser();
    }
@GetMapping("/getAllAccessByRole")
    public List<AuthAccessList> getAllAccessByUserRole()
    {

        return authenticationService.getAllAccessByUserRole();

    }

    @GetMapping("/getAllAccessByRoleName")
    public List<AuthAccessList> getAllAccessByUserRole(@RequestParam String rolename)
    {

        return authenticationService.getAllAccessByUserRoleNeme(rolename);

    }


}
