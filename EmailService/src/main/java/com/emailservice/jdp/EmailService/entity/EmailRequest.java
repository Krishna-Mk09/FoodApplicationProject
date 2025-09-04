package com.emailservice.jdp.EmailService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateAndTime;
    private String otp;
    @Transient
    private Map<String, Object> templateModel;
}
