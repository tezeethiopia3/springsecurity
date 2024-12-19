package com.security.springsecurity.controller;

import com.security.springsecurity.dto.CustomerDto;
import com.security.springsecurity.entity.Customer;
import com.security.springsecurity.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {
    private  final CustomerService customerService;
    @PostMapping("/saveCustomer")
    public Customer saveCustomer(@RequestBody CustomerDto customer){
        return customerService.saveCustomer(customer);
    }
@GetMapping("/getAllCustomer")
    public List<CustomerDto> getALlCustomer()
    {
        return customerService.getALlCustomer();

    }
}
