package com.security.springsecurity.service;


import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.daoauth.TokenRepository;
import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.dto.*;
import com.security.springsecurity.entity.*;
import com.security.springsecurity.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

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
                .roles(Set.of(userRole))
                .createdDate(LocalDateTime.now()) //added on nob=vember 18
                .build();
        try{
          RegisterResponse registerResponse=  toRegisterResponse(userRepository.save(user));
//            sendValidationEmail(user);
            return registerResponse;

        }catch(Exception exception){

            RegisterResponse registerResponse=new RegisterResponse();
            registerResponse.setResult(1);
            registerResponse.setErrorMessage("THere is database Issue");
            return registerResponse;
        }

//       return Optional.of(toRegisterResponse(userRepository.save(user))).orElseThrow(() ->
//       {return new DataIntegrityViolationException("Database error");
//       }) ;
//        sendValidationEmail(user); letter this one should be enabled
    }

 public  RegisterResponse   toRegisterResponse(AuthUser user)
    {

        return RegisterResponse.builder().result(0).email(user.getEmail()).userId(user.getId()).build();


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
        //for testing
        List<String> stringsroles=new ArrayList<>();
        if(auth!=null){
            for(GrantedAuthority x :auth.getAuthorities()){
                System.out.println(x.getAuthority());
                stringsroles.add(x.getAuthority());
            }
        }
        System.out.println("AuthenticationService==  after auth");

        Map<String, Object> claims = new HashMap<>(); //I have the type of claims from Var to Map<String, Object>
//        var user = (auth.getPrincipal());
//        claims.put("fullName", user.);
//        var jwtToken = jwtService.generateToken(claims, (AuthUser) auth.getPrincipal());
        var jwtToken = jwtService.generateToken(claims, (UserDetails) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(stringsroles)
                .firstName(auth.getName())
                .build();
    }

    @Transactional
    public ConfirmOtpResponse activateAccount(OtpConfirm otpConfirm) throws MessagingException {
        AuthToken savedToken = tokenRepository.findByToken(otpConfirm.getOtp())
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        System.out.println("Inside AuthenticationService== "+savedToken.getUser().getName());
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }
        if(otpConfirm.getUserId()!=savedToken.getUser().getId()){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token user Id not equal to fetched user id. A new token has been send to the same email address");

        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
       return toConfirmOtp(tokenRepository.save(savedToken));
    }
    public ConfirmOtpResponse toConfirmOtp(AuthToken authToken)
    {

        return ConfirmOtpResponse.builder()
                .result(0)
                .otp(authToken.getToken())
                .userId(authToken.getUser().getId())
                .username(authToken.getUser().getUsername())
                .build();


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

    private Optional<AuthToken> SaveActivationTokenForForgetPassword(AuthUser user, String newToken) {
//        String generatedToken = generateActivationCode(6);
        var token = AuthToken.builder()
                .token(newToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();


        return  Optional.of(tokenRepository.save(token));
    }

    private void sendForgetPasswordEmail(AuthUser user,String token) throws MessagingException {
        System.out.println("inside sendForgetPasswordEmail==== ");
        EmailProperties emailProperties=new EmailProperties();
        emailProperties.setSubject("Account activation");
        emailProperties.setTo(user.getEmail());
        emailProperties.setUsername(user.fullNmae());
        emailProperties.setConfirmationUrl(activationUrl);
        emailProperties.setActivationCode(token);
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

   public Optional<AuthUser> grantRoleToUser(RoleGrantToUserRequest roleGrantToUserRequest)
//public Optional<AuthUser> grantRoleToUser(AuthUser authUser)
    {
//        System.out.println("userNew.getEmail()=="+roleGrantToUserRequest.getUserEmail());
        Optional<AuthUser> user1=userRepository.findByEmail(roleGrantToUserRequest.getUserEmail());

        Set<AuthRole> authRoleList=new HashSet<>();
        
        if(user1.isEmpty()){
           throw new RuntimeException("User Should exist to provide role");
        }else{
            System.out.println("Tezezzzzzz 1=="+user1.get().getEmail());
            AuthUser authUser=new AuthUser();
            authUser=user1.get();
            System.out.println("Tezezzzzzz 2=="+roleGrantToUserRequest.getRoleNames().size());

            for(int i=0;i<roleGrantToUserRequest.getRoleNames().size();i++){
                Optional<AuthRole> role=roleRepository.findByName(roleGrantToUserRequest.getRoleNames().get(i).getName());
                System.out.println("Tezezzzzzz 2=="+role.get().getName());
                if(role.isEmpty()){
                    authRoleList.add(role.get());
                    
                }
               else
//                   if(!roleGrantToUserRequest.getRoleNames().get(i).getName().equals(role.get().getName()))
                   {
                    AuthRole authRole=new AuthRole();
                    authRole.setId(role.get().getId());
                    authRole.setName(role.get().getName());
                    authRole.setAccessLists(role.get().getAccessLists());
                    authRoleList.add(authRole);



//                    authRole.set
                }
                authRoleList.addAll(user1.get().getRoles());

//                user1.get().setRoles(authRoleList);
                authUser.setRoles(authRoleList);
//                user1.get().setRoles(authRoleList);
                userRepository.save(authUser);
//               Optional.of(userRepository.save(authUser));
                return null;
            }

        }
//        AuthUser user2=new AuthUser();
//        if(user1.isPresent()){
//            System.out.println("user1.isPresent()=="+user1.isPresent());
//            user2=user1.get();
//            authRoleList=new ArrayList<>();
//            for(RoleName role:roleGrantToUserRequest.getRoleNames()){
//                AuthRole authRole=new AuthRole();
//                authRole.setName(role.getName());
//                authRole.setCreatedDate(LocalDateTime.now());
//                authRoleList.add(authRole);
//
//            }
//        }
//        for(AuthRole authRole:user1.get().getRoles()){
//            authRoleList.add(authRole);
//        }
//
//        user1.get().setRoles(authRoleList);
//        return Optional.of(userRepository.save(user1.get()));
        return  null;
    }

//    public Optional<UserList> findByIdFInction(Integer id) {
//        System.out.println("Inside service==========");
//        return null;
//        return Optional.ofNullable(userRepository.findById(id)).orElse(Optional.empty());
//    }

public AuthRole saveRole(AuthRole authRole)
{
    Optional<AuthRole> authRole1=roleRepository.findByName(authRole.getName());
    List<AuthAccessList> authAccessLists=new ArrayList<>();
    if(authRole1.isEmpty()) {
        authRole.setCreatedDate(LocalDateTime.now());
        if(!authRole.getAccessLists().isEmpty()){
            for(int i=0;i<authRole.getAccessLists().size();i++){
                Optional<AuthAccessList> authAccessList = authAccessListRepository.findByName(authRole.getAccessLists().get(i).getName());
                authAccessList.ifPresent(authAccessLists::add);

            }
        }
//        else{
//            return roleRepository.save(authRole);
//        }
//
        authRole.setAccessLists(authAccessLists);
        authRole.setLastModifiedDate(LocalDateTime.now());
        return roleRepository.save(authRole);
    }else{
        authRole.setId(authRole1.get().getId());
//        authRole.setName(authRole1.get().getName());
        authRole.setCreatedDate(LocalDateTime.now());
        if(!authRole.getAccessLists().isEmpty()){
            for(int i=0;i<authRole.getAccessLists().size();i++){
                Optional<AuthAccessList> authAccessList = authAccessListRepository.findByName(authRole.getAccessLists().get(i).getName());
                authAccessList.ifPresent(authAccessLists::add);

            }
        }
//        else{
//            return roleRepository.save(authRole);
//        }
//
        authRole.setAccessLists(authAccessLists);
        authRole.setLastModifiedDate(LocalDateTime.now());
        return roleRepository.save(authRole);
//        authRole.setId(authRole1.get().getId());
//        authRole.setName(authRole1.get().getName());
//        authRole.setCreatedDate(authRole1.get().getCreatedDate());
//        authRole.setAccessLists(authRole1.get().getAccessLists());
//        if(!authRole1.get().getAccessLists().isEmpty()){
//            for(int i=0;i<authRole1.get().getAccessLists().size();i++){
//                Optional<AuthAccessList> authAccessList = authAccessListRepository.findByName(authRole1.get().getAccessLists().get(i).getName());
//                if(!authAccessList.isEmpty()){
//                    authAccessLists.add(authAccessList.get());
//                }else {
//                    AuthAccessList authAccessList1=new AuthAccessList();
//                    authAccessList1.setId(authAccessList.get().getId());
//                    authAccessList1.setName(authAccessList.get().getName());
//                    authAccessList1.setHaveAdd(authAccessList.get().isHaveAdd());
//                    authAccessList1.setHaveDelete(authAccessList.get().isHaveDelete());
//                    authAccessList1.setHaveUpdate(authAccessList.get().isHaveUpdate());
//                    authAccessList1.setHaveCreate(authAccessList.get().isHaveCreate());
//                    authAccessLists.add(authAccessList1);
//                }
//
//            }
//        }
//        authRole.setAccessLists(authAccessLists);
//        return roleRepository.save(authRole);
    }
}

//      @PutMapping("grantAccessToRole")
        public AuthRole assignAccessToRole(AuthRole authRole)
        {
            AuthRole role=new AuthRole();

            Optional<AuthRole> authRoleOptional=roleRepository.findByName(authRole.getName());
            role.setId(authRoleOptional.get().getId());
            role.setName(authRoleOptional.get().getName());

            for(int i=0;i<authRole.getAccessLists().size();i++){
                Optional<AuthAccessList> authAccessList=authAccessListRepository.findByName(authRole.
                        getAccessLists().get(i).getName());
                List<AuthAccessList> accessLists=new ArrayList<>();
                AuthAccessList authAccessList1=new AuthAccessList();
                if(authAccessList.isPresent()){
                    authAccessList1.setId(authAccessList.get().getId());
                    authAccessList1.setName(authAccessList.get().getName());
//                    authAccessListRepository.save(authAccessList1);

//                    accessLists.add(authAccessList1);
                }
                else{
                    authAccessList1.setName(authRole.getAccessLists().get(i).getName());
//                    authAccessListRepository.save(authAccessList1);

                }
                authAccessList1.getAuthRoleList().add(role);
                authAccessListRepository.save(authAccessList1);
//                role.getAccessLists().add(authAccessList1);
//                roleRepository.save(role);
                role.setAccessLists(accessLists);
            }

          return   roleRepository.save(role);

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

    public GenericResponse passwordChange(ChangePasswordRequest changePasswordRequest)
    {

        Optional<AuthUser> user=userRepository.findByEmail(changePasswordRequest.getUsername());
        if(user.isPresent()){

            if(bCryptPasswordEncoder.matches(changePasswordRequest.getOldpassword(),user.get().getPassword())){
                System.out.println("confirmed===");
                user.get().setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewpassword()));
                userRepository.save(user.get());
                return GenericResponse.builder()
                        .errorMessage("")
                        .email(user.get().getEmail())
                        .userId(user.get().getId())
                        .build();
            }
            else{
                throw new IllegalStateException("old password is not correct");
            }
        }

        return GenericResponse.builder().errorMessage("User does not exist in the database").result(1).build();



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
    public List<AuthAccessList> getAllAccessByUserRole()
    {
        System.out.println("Service getAllAccessByUserRole===================");

       return authAccessListRepository.findByRolesAll();


    }
    public List<AuthAccessList> getAllAccessByUserRoleNeme(String roleName)
    {

        Optional<AuthRole> authRole= roleRepository.findByName(roleName);
        if(authRole.isPresent()){
            int roleId=authRole.get().getId();
           return authAccessListRepository.findByRoles(roleId);

        }

        return null;


    }


    public GenericResponse ChangePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {

        var user = (UserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

       AuthUser authUser= userRepository.findByEmail(user.getUsername()).get();


        System.out.println(authUser.getEmail());
        if(!changePasswordRequest.getUsername().equals(authUser.getUsername())){
            throw new IllegalStateException("User name is not correct");

        }
        if(!passwordEncoder.matches(changePasswordRequest.getOldpassword(),authUser.getPassword())){
            System.out.println(" not matched");
            throw new IllegalStateException("Password is not correct");

        }
        if(!changePasswordRequest.getNewpassword().equals(changePasswordRequest.getConfirmpassword())){
            throw new IllegalStateException("Password confirmation failed");
        }
        authUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewpassword()));

        userRepository.save(authUser);

        return GenericResponse.builder()
                .email(authUser.getUsername())
                .result(0)
                .userId(authUser.getId())
                .build();
    }

    public GenericResponse forgetPassword(UserEmail userEmail) throws MessagingException {
        System.out.println("userEmail==="+userEmail);
        Optional<AuthUser> user=userRepository.findByEmail(userEmail.getUserEmail());
        if(user.isEmpty()){
            System.out.println("user======user.isEmpty()==");
            return GenericResponse.builder()
//                    .userId(authUser.getId())
//                    .email(authUser.getEmail())
                    .errorMessage("User id is not found")
                    .result(1)
                    .build();
        }
        else if(user.get().getEmail()!=null) {
            String generatedToken = generateActivationCode(6);

            AuthUser authUser=user.get();
            Optional<AuthToken> authToken = SaveActivationTokenForForgetPassword(authUser,generatedToken);
//            authUser.setPassword(passwordEncoder.encode(authToken.getToken()));
//            userRepository.save(authUser);
            if(authToken.isPresent()) {
                sendForgetPasswordEmail(authUser, generatedToken);

                return GenericResponse.builder()
                        .userId(authUser.getId())
                        .email(authUser.getEmail())
                        .errorMessage("")
                        .result(0)
                        .build();
            }else{
                return GenericResponse.builder()
                        .errorMessage("User id is not found")
                        .result(1)
                        .build();
            }

        }
        else{
            return GenericResponse.builder()
//                    .userId(authUser.getId())
//                    .email(authUser.getEmail())
                    .errorMessage("User id is not found")
                    .result(1)
                    .build();
        }


    }

    public GenericResponse UpdatePassword(UpdatePasswordRequest updatePasswordRequest) throws MessagingException {

        Optional<AuthUser> authUser=  userRepository.findByEmail(updatePasswordRequest.getUsername());
        if(authUser.isEmpty()){
            return genericResponseFunction(1,"User id is not found");

        }
        Optional<AuthToken> authToken=   tokenRepository.findByToken(updatePasswordRequest.getOtpText());
        if(authToken.isEmpty()){
            return genericResponseFunction(1,"Otp id is not found");
        }
        if(LocalDateTime.now().isAfter(authToken.get().getExpiredAt())){
            sendValidationEmail(authToken.get().getUser());
            return genericResponseFunction(1,"Otp is expired an new OTP is sent to your email address");
        }
        AuthUser user=authToken.get().getUser();
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
        userRepository.save(user);


       return genericResponseFunction(0,"Password Successfully Updated");
    }

  public GenericResponse  genericResponseFunction(int result, String errorMessage)
    {
        return GenericResponse.builder()
                .errorMessage(errorMessage)
                .result(result)
                .build();

    }

}
