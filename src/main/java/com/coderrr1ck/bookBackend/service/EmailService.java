package com.coderrr1ck.bookBackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendEmail(String to,String username,String activationCode,String subject,String confirmationUrl) throws MessagingException {
        String templateName = "activate-account";
        Map<String,Object> properties = new HashMap<>();
        properties.put("username",username); // this and template variable should be same
        properties.put("activation_code",activationCode);
        properties.put("confirmationUrl",confirmationUrl);
        Context ctx = new Context();
        ctx.setVariables(properties);
        String template = springTemplateEngine.process(templateName,ctx);
//        this creates a final template

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setFrom("coderrr1ck@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(template,true);
        javaMailSender.send(mimeMessage);

    }
}
