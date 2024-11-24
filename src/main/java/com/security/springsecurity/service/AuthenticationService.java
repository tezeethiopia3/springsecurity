package com.security.springsecurity.service;


import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.daoauth.TokenRepository;
import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.dto.*;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthToken;
import com.security.springsecurity.entity.AuthUser;
import com.security.springsecurity.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
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
                .enabled(true) //enabled using account activation end point // for HPMIS purpose it is true
                .roles(List.of(userRole))
                .createdDate(LocalDateTime.now()) //added on nob=vember 18
                .build();
        userRepository.save(user);
//        sendValidationEmail(user); letter this one should be enabled
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

//    @Transactional
//    public void activateAccount(String token) throws MessagingException {
//        Token savedToken = tokenRepository.findByToken(token)
//                // todo exception has to be defined
//                .orElseThrow(() -> new RuntimeException("Invalid token"));
//        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
//            sendValidationEmail(savedToken.getUser());
//            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
//        }
//
//        var user = userRepository.findById(savedToken.getUser().getId())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        savedToken.setValidatedAt(LocalDateTime.now());
//        tokenRepository.save(savedToken);
//    }

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

//    private void sendValidationEmail(User user) throws MessagingException {
//        var newToken = generateAndSaveActivationToken(user);
//
//        emailService.sendEmail(
//                user.getEmail(),
//                user.fullNmae(),
//                EmailTemplateName.ACTIVATE_ACCOUNT,
//                activationUrl,
//                newToken,
//                "Account activation"
//        );
//    }


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

   public List<AuthUser> findAllUserForTest()
    {
        return null;

//        return userRepository.findAll();
    }

//    public Optional<UserList> findByIdFInction(Integer id) {
//        System.out.println("Inside service==========");
//        return null;
//        return Optional.ofNullable(userRepository.findById(id)).orElse(Optional.empty());
//    }



    public ResponseEntity<?> createRole(Role role)
    {
        AuthRole authRole=new AuthRole();
        authRole.setCreatedDate(LocalDateTime.now());
        authRole.setName(role.getRoleName());
        roleRepository.save(authRole);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> createPermssion(AuthAccessList permission){
        authAccessListRepository.save(permission) ;

        return ResponseEntity.accepted().build();
    }


}
