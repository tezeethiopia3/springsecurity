package com.security.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("manager")
@RestController
public class ManagerController {

    @GetMapping("/manage")
    public String mangerGetFUnction()
    {
        return  "Manager get method";
    }

    @PostMapping("/manage")
    public String mangerPostFunction()
    {
        return  "Manager post method";
    }
}
