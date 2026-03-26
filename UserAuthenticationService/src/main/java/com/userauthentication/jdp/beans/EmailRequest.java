package com.userauthentication.jdp.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateAndTime;
    private String otp;
}
