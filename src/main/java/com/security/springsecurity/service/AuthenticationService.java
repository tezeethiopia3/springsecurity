package com.security.springsecurity.service;


import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.daoauth.TokenRepository;
import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.dto.*;
import com.security.springsecurity.entity.*;
import com.security.springsecurity.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;
    private final AuthAccessListRepository authAccessListRepository;
    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public RegisterResponse register(RegistrationRequest request) throws MessagingException {
        System.out.println("AuthenticationService==  register register");
//        AuthRole userRole = roleRepository.findByName("USER")
        System.out.println("request.getRoleName()==="+request.getRoleName());
        AuthRole userRole = roleRepository.findByName(request.getRoleName())

                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE was not found"));
        var user = AuthUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false) // will be enabled
                .roles(List.of(userRole))
                .createdDate(LocalDateTime.now()) //added on nob=vember 18
                .build();
//        try{
//            return toRegisterResponse(userRepository.save(user));
//
//        }catch(Exception exception){
//
//            RegisterResponse registerResponse=new RegisterResponse();
//            registerResponse.setResult(1);
//            registerResponse.setErrorMessage(exception.getMessage());
//            return registerResponse;
//        }

       return Optional.of(toRegisterResponse(userRepository.save(user))).orElseThrow(() ->
       {return new DataIntegrityViolationException("Database error");
       }) ;
//        sendValidationEmail(user); letter this one should be enabled
    }

 public  RegisterResponse   toRegisterResponse(AuthUser user)
    {

        return RegisterResponse.builder().result(0).email(user.getEmail()).build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("AuthenticationService==  register register");
        System.out.println("AuthenticationService==  request.getEmail()"+request.getEmail());
        System.out.println("AuthenticationService==  register register"+request.getPassword());
        Authentication auth=null;
        try {
             auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception ex){
            System.out.println("Inside the exception=====of the user authentication");
            System.out.println("The exception is ==="+ex.getLocalizedMessage());
            System.out.println("The exception is ==="+ex.getMessage());
        }

        System.out.println("AuthenticationService==  after auth");

        Map<String, Object> claims = new HashMap<>(); //I have the type of claims from Var to Map<String, Object>
//        var user = (auth.getPrincipal());
//        claims.put("fullName", user.);



//        var jwtToken = jwtService.generateToken(claims, (AuthUser) auth.getPrincipal());
        var jwtToken = jwtService.generateToken(claims, (UserDetails) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        AuthToken savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        System.out.println("Inside AuthenticationService== "+savedToken.getUser().getName());
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateAndSaveActivationToken(AuthUser user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = AuthToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(AuthUser user) throws MessagingException {
        System.out.println("inside sendValidationEmail==== ");
        var newToken = generateAndSaveActivationToken(user);
        EmailProperties emailProperties=new EmailProperties();
        emailProperties.setSubject("Account activation");
        emailProperties.setTo(user.getEmail());
        emailProperties.setUsername(user.fullNmae());
        emailProperties.setActivationCode(newToken);
        emailProperties.setConfirmationUrl(activationUrl);
        emailService.sendMail(emailProperties);

//        emailService.sendEmail(
//                EmailProperties.builder()
//                        .to(user.getEmail())
//                        .activationCode(newToken)
//                        .confirmationUrl(activationUrl)
//                        .subject("Account activation")
//                        .username(user.fullNmae()).build()        );
    }


    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

   public Optional<AuthUser> grantRoleToUser(AuthUser userNew)
    {
        System.out.println("userNew.getEmail()=="+userNew.getEmail());
        Optional<AuthUser> user1=userRepository.findByEmail(userNew.getEmail());
        List<AuthRole> authRoleList=null;
        AuthUser user2=new AuthUser();
        if(user1.isPresent()){
            System.out.println("user1.isPresent()=="+user1.isPresent());
            user2=user1.get();
            authRoleList=new ArrayList<>();
            for(AuthRole role:userNew.getRoles()){

                Optional<AuthRole> optionalAuthRole=roleRepository.findByName(role.getName());
                if(optionalAuthRole.isPresent()){
                    System.out.println("optionalAuthRole.isPresent()=="+optionalAuthRole.isPresent());
                    authRoleList.add(optionalAuthRole.get())  ;
                }else{
                    AuthRole authRole=new AuthRole();
//
                    authRole.setName(role.getName());
                    authRole.setCreatedDate(LocalDateTime.now());
                    authRoleList.add(authRole);
                }

            }
        }
        user2.setRoles(authRoleList);
        return Optional.of(userRepository.save(user2));
    }

//    public Optional<UserList> findByIdFInction(Integer id) {
//        System.out.println("Inside service==========");
//        return null;
//        return Optional.ofNullable(userRepository.findById(id)).orElse(Optional.empty());
//    }

public AuthRole saveRole(AuthRole authRole)
{
    authRole.setCreatedDate(LocalDateTime.now());
    Optional<AuthAccessList> authAccessList=authAccessListRepository.findByName(authRole.getAccessLists().iterator().next().getName());
   if(authAccessList.isPresent()){
       System.out.println("authAccessList.get().getId()===="+authAccessList.get().getId());
       authRole.setAccessLists(authAccessList.stream().collect(Collectors.toList()));
   }
    return roleRepository.save(authRole) ;
}


    public Optional<AuthRole> updateRole(AuthRole rolNew) {
        Optional<AuthRole> optionalAuthRole = roleRepository.findByName(rolNew.getName());
        AuthRole authRole =new AuthRole();
        List<AuthAccessList> finalList = null;
        if (optionalAuthRole.isPresent()) {
            System.out.println("Is present=================");
            authRole = optionalAuthRole.get();
            finalList = new ArrayList<>();
            for (AuthAccessList accessList : rolNew.getAccessLists()) {
                Optional<AuthAccessList> authAccessList=authAccessListRepository.findByName(accessList.getName());
                if (authAccessList.isPresent()) {
                    System.out.println("is present from the database accessList.getName()==========" + accessList.getName());
                    finalList.add(authAccessList.get());
                } else {
                    System.out.println("Else part ==============");
                    AuthAccessList authAccessList2 = new AuthAccessList();
                    authAccessList2.setName(accessList.getName());
                    finalList.add(authAccessList2);
                }

            }

        }
        authRole.setAccessLists(finalList);
        return Optional.of(roleRepository.save(authRole));

    }

    public AuthAccessList  saveResource(AuthAccessList authAccessList){
         return authAccessListRepository.save(authAccessList)   ;
    }

    public Optional<AuthAccessList>  findAccessByName(String access){
       return authAccessListRepository.findByName(access);
    }

public void grantRoleToUser(){

}

    public Boolean passwordChange(PasswordRequestUtil passwordRequestUtil)
    {

        Optional<AuthUser> user=userRepository.findByEmail(passwordRequestUtil.getUserEmail());
        if(user.isPresent()){

            if(bCryptPasswordEncoder.matches(passwordRequestUtil.getOldPassword(),user.get().getPassword())){
                System.out.println("confirmed===");
                user.get().setPassword(bCryptPasswordEncoder.encode(passwordRequestUtil.getNewPassword()));
                userRepository.save(user.get());
                return true;
            }
            else{
                System.out.println("not confirmed");
                return false;
            }
        }

        return false;



    }
   public List<UserDto> getAllUser(){
        return toUserDto(userRepository.findAll());
    }
    public List<UserDto> toUserDto(List<AuthUser> authUsers){
        List<UserDto> userDtos=new ArrayList<>();
        for(AuthUser user:authUsers){
            UserDto userDto=new UserDto();
            userDto.setAccountLocked(user.isAccountLocked());
            userDto.setEnabled(user.isEnabled());
            userDto.setCreatedDate(user.getCreatedDate());
            userDto.setLastName(user.getLastName());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastModifiedDate(user.getLastModifiedDate());
            userDto.setDateOfBirth(user.getDateOfBirth());
            userDtos.add(userDto);
        }
        return userDtos;

    }
    


}
