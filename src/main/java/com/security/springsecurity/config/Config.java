package com.security.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Config {
	@Bean
	Jaxb2Marshaller jaxb2Marshaller()
	{
		Jaxb2Marshaller jaxb2Marshaller=new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan("com.security.springsecurity");
		
		return jaxb2Marshaller;
	}

//	@Bean
//	public JavaMailSender javaMailSender() {
//		return new JavaMailSenderImpl();
//	}

}
