package com.security.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employee")
public class TestController {
@GetMapping("/test")
   public ResponseEntity<String>  fun()
    {
        return ResponseEntity.ok("Tezera is working Hard");
    }
}
