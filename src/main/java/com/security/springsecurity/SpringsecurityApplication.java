package com.security.springsecurity;

import com.security.springsecurity.daoauth.AuthAccessListRepository;
import com.security.springsecurity.daoauth.RoleRepository;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);

		System.out.println("working thanks God============== test");
	}

//	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository)
	{
		return  args -> {
			if(roleRepository.findByName("ADMIN").isEmpty()){
				LocalDateTime localDateTime1 = LocalDateTime.now().plusYears(1)
						.plusMonths(1)
						.plusWeeks(1)
						.plusDays(1);
				roleRepository.save(AuthRole.builder().name("ADMIN").createdDate(LocalDateTime.now()).


						build());
			};
//			if(authAccessList.findByName("USER_READ").isEmpty()){
//				LocalDateTime localDateTime1 = LocalDateTime.now().plusYears(1)
//						.plusMonths(1)
//						.plusWeeks(1)
//						.plusDays(1);
//				AuthRole authRole=new AuthRole();
//				authRole.setId(1);
//				authAccessList.save(AuthAccessList.builder().Name("USER").authRole(authRole).
//
//
//						build());
//			}

		};

	}


}
