package com.emailservice.jdp.EmailService.controller;


import com.emailservice.jdp.EmailService.entity.EmailRequest;
import com.emailservice.jdp.EmailService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailService")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok("Email Sent Successfully");
    }
}
