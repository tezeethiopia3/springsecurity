package com.security.springsecurity.dto;

import com.security.springsecurity.service.EmailTemplateName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailProperties {
    String to;
    String username;
    String confirmationUrl;
    String activationCode;
    String subject;
    EmailTemplateName emailTemplate;
}
