package com.security.springsecurity.service;

import com.security.springsecurity.daoauth.CustomerRepository;
import com.security.springsecurity.dto.CustomerDto;
import com.security.springsecurity.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDto customerDto){

        return customerRepository.save(toCustomer(customerDto));
    }

    public Customer deleteCustomer(String code)
    {
        Optional<Customer> customer=customerRepository.getByCode(code);
        if(customer.isPresent()){
             customerRepository.delete(customer.get());
             return  customer.get();
        }else
        {
          return null;
        }


    }
    public Customer updateCustomer(CustomerDto customerDto)
    {
        Optional<Customer> customer=customerRepository.getByCode(customerDto.getCode());

        if(customer.isPresent()){
            Customer customer1=customer.get();

            customerRepository.save(Customer.builder()
                            .isActive(customerDto.isActive())
                            .creditLimit(customerDto.getCreditLimit())
                            .id(customer1.getId())
                            .code(customerDto.getCode())
                            .phone(customerDto.getPhone())
                            .name(customerDto.getName())
                            .email(customerDto.getEmail())
                            .statusName(customerDto.getStatusName())
                    .build());
            return  customer1;
        }else
        {
            return null;
        }


    }

  public Customer  toCustomer(CustomerDto customerDto)
    {
        return Customer.builder().
                email(customerDto.getEmail())
                .name(customerDto.getName())
                .phone(customerDto.getPhone())
                .code(customerDto.getCode())
                .creditLimit(customerDto.getCreditLimit())
                .statusName(customerDto.getStatusName())
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
                    .statusName(customer.getStatusName())
                    .isActive(customer.isActive())
                    .build();

            customerList.add(customer1);
        }
        return customerList;
    }

    public Optional<Customer> getByCode(String code){
        return customerRepository.getByCode(code);
    }

}
