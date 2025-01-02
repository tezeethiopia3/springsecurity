package com.security.springsecurity.controller;

import com.security.springsecurity.dto.GenericResponse;
import com.security.springsecurity.dto.RoleResponse;
import com.security.springsecurity.dto.UpdateUserRoleRequest;
import com.security.springsecurity.dto.UserDto;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthUser;
import com.security.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @RequestMapping("/getAllUser")
    public List<UserDto> getAllUser(){
        return userService.getAllUsers();
    }

@PostMapping("/updateUserRole")
    public GenericResponse updateUserRole(@RequestBody  UpdateUserRoleRequest updateUserRoleRequest){
        return  userService.updateUserRole(updateUserRoleRequest);
    }

    @PostMapping("/grantUserRole")
    public GenericResponse grantUserRole(@RequestBody  UpdateUserRoleRequest updateUserRoleRequest){
        return  userService.grantUserRole(updateUserRoleRequest);
    }
@GetMapping("/getAllRoles")
    public List<RoleResponse>  getAllRole(){
        return userService.getAllRole();
    }
@GetMapping("/getAllUserByRole")
    public List<UserDto>  getAllUserByRole(@RequestParam String rolename)
    {
        System.out.println("rolename==="+rolename);
        return  userService.getAllUserByRole(rolename);

    }
    @GetMapping("/getUserByEmail")
    public UserDto getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
}
