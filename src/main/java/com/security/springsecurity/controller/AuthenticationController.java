package com.security.springsecurity.controller;



import com.security.springsecurity.dto.*;
import com.security.springsecurity.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    static Logger logger= LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RegisterResponse register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        System.out.println("AuthenticationController==  register method");

        return service.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println("AuthenticationController==  authenticate");
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/activate-account")
    public ConfirmOtpResponse confirm(
           @RequestBody OtpConfirm otpConfirm
    ) throws MessagingException {
        System.out.println("confirm=== controller");
       return service.activateAccount(otpConfirm);
    }
    @PostMapping("/forgetpassword")
    public GenericResponse forgetPassword(@RequestBody UserEmail userEmail) throws MessagingException {
        System.out.println("userEmail==="+userEmail.getUserEmail());
      return  service.forgetPassword(userEmail);
    }
    @GetMapping("/test")
    public String testFunction()
    {
        return "Tezera is joining to best remote programmer";
    }
//@GetMapping("/alluser")
//    ResponseEntity<List<AuthUser>> findAllUserForTest()
//    {
////        return  ResponseEntity.ok(service.findAllUserForTest()) ;
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.findAllUserForTest());
//    }

//    @GetMapping("/userById/{id}")
//   public UserList getUserByIdFunction(@PathVariable Integer id)
//    {
//        System.out.println("getUserById===");
//        System.out.println("getUserById==="+id);
//        Optional<UserList> userOptional=service.findByIdFInction(id);
//        if(userOptional.isPresent()){
//            return service.findByIdFInction(id).get();
//        }
//        else {
//
//        }
//        return  null;
//
//    }
@PostMapping("/updatePassword")
public GenericResponse UpdatePassword(@RequestBody  UpdatePasswordRequest updatePasswordRequest) throws MessagingException {

    return service.UpdatePassword(updatePasswordRequest);
}
}
