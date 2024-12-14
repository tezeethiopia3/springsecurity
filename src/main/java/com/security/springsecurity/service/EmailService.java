package com.security.springsecurity.service;

import com.security.springsecurity.dto.EmailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {
   private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
public void sendMail(EmailProperties emailProperties) throws MessagingException {
    String templateName;
    if (emailProperties.getEmailTemplate() == null) {
        templateName = "confirm-email";
    } else {
        templateName = emailProperties.getEmailTemplate().getName();
    }
    MimeMessage mimeMessage= mailSender.createMimeMessage();
    MimeMessageHelper helper=new MimeMessageHelper(
            mimeMessage,
            MULTIPART_MODE_MIXED,
            UTF_8.name()
    );
    helper.setFrom("tezeethiopia@gmail.com");
    helper.addTo("tezeethiopia3@gmail.com");
    helper.setSubject("Tezera you are working Hard");
    helper.setText("Tezera");
    Map<String, Object> properties = new HashMap<>();
    properties.put("username", "Tezera Demissie");
    properties.put("confirmationUrl", emailProperties.getConfirmationUrl());
    properties.put("activation_code", emailProperties.getActivationCode());

    Context context = new Context();
    context.setVariables(properties);
    String template = templateEngine.process("activate_account", context);

    helper.setText(template, true);
    mailSender.send(mimeMessage);



}

}
