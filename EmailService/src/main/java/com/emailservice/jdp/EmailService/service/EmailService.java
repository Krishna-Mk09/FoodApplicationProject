package com.emailservice.jdp.EmailService.service;

import com.emailservice.jdp.EmailService.entity.EmailRequest;

/*
 * Author Name : M.V.Krishna
 * Date: 26-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
public interface EmailService {

    String sendEmail(EmailRequest request);
}
