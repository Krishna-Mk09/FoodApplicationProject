package com.emailservice.jdp.EmailService.service;

import com.emailservice.jdp.EmailService.entity.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public String sendEmail(EmailRequest request) {
        Context context = new Context();
        try {
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("EMAIL", request.getSenderEmail());
            templateModel.put("IP", request.getIpaddress());
            templateModel.put("Device", request.getDevice());
            templateModel.put("DateAndTime", java.time.LocalDateTime.now().toString());
            templateModel.put("OTP", request.getOtp()==null? "":request.getOtp());
            request.setTemplateModel(templateModel);
            context.setVariables(request.getTemplateModel());
            String htmlContent = templateEngine.process(request.getTemplateName(), context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(request.getSenderEmail());
            helper.setSubject(request.getSubject());
            helper.setText(htmlContent, true);
            helper.setFrom("zomaato.fap@gmail.com", "Food Application Platform");
            helper.setReplyTo("zomaato.fap@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw e;
        }
        return "Email Sent Successfully";
    }
}
