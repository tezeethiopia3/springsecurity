package com.security.springsecurity.exception;

import com.security.springsecurity.dto.RegisterResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GLobalExceptionHandler {

@ExceptionHandler(LockedException.class)
ResponseEntity<ExceptionResponse> handleLockedException(LockedException exp){
    return
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            ExceptionResponse.builder()
                                    .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                                    .businessErrorDescription(BusinessErrorCodes.INCORRECT_CURRENT_PASSWORD.getDescription())
                                    .error(exp.getMessage())
                                    .build()
                    );
    }
    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException exp){
        return
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ExceptionResponse> handleBadCredentialException(BadCredentialsException exp){
        return
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                                        .businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }

    @ExceptionHandler(MessagingException.class)
    ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException exp){
        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .error(exp.getMessage())
                                        .build()
                        );
    }
    @ExceptionHandler(MailConnectException.class)
    ResponseEntity<ExceptionResponse> handleMessagingException(MailConnectException exp){
        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .error(exp.getMessage())
                                        .build()
                        );
    }

    /*
    MethodArgumentNotValidException is thrown when the validation is broken. Actually it is thrown by @Valid Annotation
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handleMessagingException(MethodArgumentNotValidException exp){
        Set<String> errors=new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    var errorMessage=error.getDefaultMessage();
                    errors.add(errorMessage);
                }
        );
        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .validationErrors(errors)
                                        .build()
                        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exp){

        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorDescription(BusinessErrorCodes.DATA_NOY_FOUND.getDescription())
                                        .businessErrorCode(BusinessErrorCodes.DATA_NOY_FOUND.getCode())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ExceptionResponse> PasswordException(IllegalStateException exp){

        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorDescription(BusinessErrorCodes.PASSWORD_FAILED.getDescription())
                                        .businessErrorCode(BusinessErrorCodes.PASSWORD_FAILED.getCode())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    RegisterResponse handleDataIntegrityViolationException(DataIntegrityViolationException exp){

        return RegisterResponse.builder().result(1).errorMessage("There is Database Error").build();
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exp){

        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorDescription(BusinessErrorCodes.VALIDATION_FAILED.getDescription())
                                        .businessErrorCode(BusinessErrorCodes.VALIDATION_FAILED.getCode())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    RegisterResponse handleDataIntegrityViolationException(DataIntegrityViolationException exp){
//
//        return RegisterResponse.builder().result(1).errorMessage("There is Database Error").build();
//    }



    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handleMessagingException(Exception exp){

        return
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ExceptionResponse.builder()
                                        .businessErrorDescription("Internal server error please contact system admin")
                                        .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                                        .error(exp.getMessage())
                                        .build()
                        );
    }

}
