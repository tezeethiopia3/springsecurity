package com.security.springsecurity.service;

import com.security.springsecurity.daoauth.CustomerRepository;
import com.security.springsecurity.dto.CustomerDto;
import com.security.springsecurity.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDto customerDto){

        return customerRepository.save(toCustomer(customerDto));
    }

  public Customer  toCustomer(CustomerDto customerDto)
    {
        return Customer.builder().
                email(customerDto.getEmail())
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .code(customerDto.getCode())
                .creditLimit(customerDto.getCreditLimit())
                .isActive(customerDto.isActive())
                . build();
    }

    public List<CustomerDto> getALlCustomer()
    {
        return toCustomerDto(customerRepository.findAll());

    }


    public List<CustomerDto>  toCustomerDto(List<Customer> customers)
    {
        List<CustomerDto> customerList=new ArrayList<>();
        for(Customer customer:customers){
            CustomerDto customer1 = CustomerDto.builder().
                    email(customer.getEmail())
                    .name(customer.getName())
                    .phone(customer.getPhone())
                    .code(customer.getCode())
                    .creditLimit(customer.getCreditLimit())
                    .isActive(customer.isActive())
                    .build();
            customerList.add(customer1);
        }
        return customerList;
    }

}
