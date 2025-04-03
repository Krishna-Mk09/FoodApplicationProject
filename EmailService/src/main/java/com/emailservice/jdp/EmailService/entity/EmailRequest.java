package com.emailservice.jdp.EmailService.entity;

import jakarta.persistence.Transient;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailRequest {
    private String senderEmail;
    private String subject;
    private String templateName;
    private String userName;
    private String ipaddress;
    private String device;
    private String dateAndTime;

    private String otp;
    @Transient
    private Map<String, Object> templateModel;
}
