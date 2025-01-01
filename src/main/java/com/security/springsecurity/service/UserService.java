package com.security.springsecurity.service;

import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.dto.*;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public List<UserDto> getAllUsers()
    {
        return toUserDto(userRepository.findAll()) ;
    }
    public List<UserDto> toUserDto(List<AuthUser> user){
        List<UserDto> userDtos=new ArrayList<>();
        for(AuthUser authUser:user){
            userDtos.add(UserDto.builder()
                    .lastModifiedDate(authUser.getLastModifiedDate())
                    .email(authUser.getEmail())
                    .dateOfBirth(authUser.getDateOfBirth())
                    .lastName(authUser.getLastName())
                    .authRoleSet(authUser.getRoles().stream().map(AuthRole::getName).toList())
                    .firstName(authUser.getFirstName())
                    .createdDate(authUser.getCreatedDate())
                    .enabled(authUser.isEnabled())
                    .accountLocked(authUser.isAccountLocked())

                    .build());
        }
        return userDtos;
    }

  public GenericResponse  updateUserRole(UpdateUserRoleRequest updateUserRoleRequest){
        Optional<AuthUser> user=userRepository.findByEmail(updateUserRoleRequest.getEmail());
      System.out.println("user.isPresent()==="+user.isPresent());
        if(user.isPresent()){
            AuthUser authUser=new AuthUser();
            authUser=user.get();
            authUser.setLastName("mussie");

            List<AuthRole> roleList=roleRepository.findAllRolesOfUser(updateUserRoleRequest.getEmail());
           Optional<AuthRole> optionalAuthRole=roleRepository.findByName(updateUserRoleRequest.getNewRole());

            List<AuthRole> roles=new ArrayList<>();
            System.out.println("roleList.size()==="+roleList.size());
           for(int i=0;i<roleList.size();i++){

                   AuthRole authRole=new AuthRole();
                   System.out.println("optionalAuthRole.get().getId()=="+optionalAuthRole.get().getId());
                   System.out.println("optionalAuthRole.get().getName()=="+optionalAuthRole.get().getName());

               System.out.println("roleList.get(i).getName()==="+roleList.get(i).getName());
               System.out.println("updateUserRoleRequest.getOldRole()==="+updateUserRoleRequest.getOldRole());
               if(roleList.get(i).getName().equals(updateUserRoleRequest.getOldRole())){
                   authRole.setId(optionalAuthRole.get().getId());
                   authRole.setName(optionalAuthRole.get().getName());

               }
               else{
                   authRole.setId(roleList.get(i).getId());
                   authRole.setName(roleList.get(i).getName());


               }
               roles.add(authRole);

           }
           authUser.setRoles(roles);
          AuthUser user1= userRepository.save(authUser);
          if(user1.getId()!=null){
              return  GenericResponse.builder()
                      .result(0)
                      .userId(0)
                      .errorMessage("User does not exist")
                      .email(updateUserRoleRequest.getEmail())
                      .build();
          }
          else {
              return  GenericResponse.builder()
                      .result(1)
                      .userId(0)
                      .errorMessage("There is an issue===========")
                      .email(updateUserRoleRequest.getEmail())
                      .build();
          }
        }else{
          return  GenericResponse.builder()
                  .result(1)
                  .userId(0)
                  .errorMessage("User does not exist")
                  .email(updateUserRoleRequest.getEmail())
                  .build();
      }

  }

    public GenericResponse  grantUserRole(UpdateUserRoleRequest updateUserRoleRequest){
        Optional<AuthUser> user=userRepository.findByEmail(updateUserRoleRequest.getEmail());
        System.out.println("user.isPresent()==="+user.isPresent());
        if(user.isPresent()){
            AuthUser authUser=new AuthUser();
            authUser=user.get();
//            authUser.setId(user.get().getId());
//            authUser.setEmail(user.get().getEmail());

            System.out.println("user.get().getRoles().size()=="+user.get().getRoles().size());
                System.out.println("updateUserRoleRequest.getRole()=="+updateUserRoleRequest.getRole());
                Optional<AuthRole>  roleOptional=roleRepository.findByName(updateUserRoleRequest.getRole());
                System.out.println("roleOptional.isPresent()=="+roleOptional.isPresent());
                AuthRole role=new AuthRole();
                if(roleOptional.isPresent()){
                    System.out.println("roleOptional.get().getName()==="+roleOptional.get().getName());
                    role.setId(roleOptional.get().getId());
                    role.setName(roleOptional.get().getName());
                }else {
                    System.out.println("Role does not exist");
                    role.setName(updateUserRoleRequest.getRole());
                }
                role.addUser(authUser);
//                role.getUsers().add(authUser);
                roleRepository.save(role);
                user.get().addRole(role);
                userRepository.save(user.get());
                return GenericResponse.builder()
                        .result(0)
                        .userId(0)
                        .errorMessage("No Error")
                        .email(updateUserRoleRequest.getEmail())
                        .build();


        }
        return  GenericResponse.builder()
                .result(1)
                .userId(0)
                .errorMessage("No Error")
                .email(updateUserRoleRequest.getEmail())
                .build();

    }

  public void  testManyToMAnyBiDirectional(RegistrationRequest authUser)
    {

        AuthUser user=new AuthUser();
        List<AuthUser> authUsers=new ArrayList<>();
        user.setEmail(authUser.getEmail());
        user.setPassword(authUser.getPassword());
        user.setCreatedDate(LocalDateTime.now());
        user.setEnabled(false);
        user.setAccountLocked(false);
        user.setAccountLocked(false);
        user.setFirstName(authUser.getFirstName());
        user.setDateOfBirth(LocalDate.now());
        user.setLastName(authUser.getLastName());
        authUsers.add(user);
        List<AuthRole> authRoleList=new ArrayList<>();
        AuthRole role=new AuthRole();
        role.setName(authUser.getRoleName());
//        for(int i=0;i<authUser.get().size();i++){
//            AuthRole authRole=new AuthRole();
//            authRole.setName(authUser.getRoles().get(i).getName());
//            authRole.setCreatedDate(LocalDateTime.now());
//            authRoleList.add(authRole);
//        }
        user.setRoles(authRoleList);
        role.setUsers(authUsers);
        userRepository.save(user);








    }

  public List<RoleResponse>  getAllRole(){
      return toRoleResponse(roleRepository.findAll());
    }
 public List<UserDto>  getAllUserByRole(String roleName)
    {
      return  toUserDto(userRepository.findUserByRoles(roleName));

    }

    List<RoleResponse> toRoleResponse(List<AuthRole> authRoleList){
        List<RoleResponse> roleResponses=new ArrayList<>();
        for(AuthRole authRole:authRoleList){
            roleResponses.add(RoleResponse.builder()
                            .name(authRole.getName())
                            .status(authRole.isStatus())
                    .build());
        }
        return  roleResponses;
    }

    public AuthUser getUserByEmail(String email){
        System.out.println("email==============="+email);
        Optional<AuthUser> user=userRepository.findByEmail(email);
        return user.orElse(null);

    }

}
