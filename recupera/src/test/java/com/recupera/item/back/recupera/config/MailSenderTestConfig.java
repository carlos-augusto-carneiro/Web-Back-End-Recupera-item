package com.recupera.item.back.recupera.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@TestConfiguration
public class MailSenderTestConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl(); // Bean fake, não envia e-mails reais
    }
} 