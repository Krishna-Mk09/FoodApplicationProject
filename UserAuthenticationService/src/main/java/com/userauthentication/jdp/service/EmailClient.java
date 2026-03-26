package com.userauthentication.jdp.service;

import com.userauthentication.jdp.beans.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * Author Name : M.V.Krishna
 * Date: 26-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@FeignClient(name = "EmailService", url = "http://localhost:8081/emailService")
public interface EmailClient {

    @PostMapping("/send-email")
    void sendEmail(@RequestBody EmailRequest emailRequest)throws Exception;
}
