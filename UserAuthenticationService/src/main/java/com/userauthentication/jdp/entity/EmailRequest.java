package com.userauthentication.jdp.entity;

import lombok.*;

import java.time.LocalDateTime;

/*
 * Author Name : M.V.Krishna
 * Date: 26-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
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
    private LocalDateTime dateAndTime;

    private String otp;

}
