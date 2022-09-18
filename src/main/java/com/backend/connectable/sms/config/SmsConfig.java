package com.backend.connectable.sms.config;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfig {

    private static final String smsApiDomain = "https://api.coolsms.co.kr";

    @Value("${sms.api-key}")
    private String smsApiKey;

    @Value("${sms.api-secret}")
    private String smsApiSecret;

    @Bean
    public DefaultMessageService initMessageService() {
        return NurigoApp.INSTANCE.initialize(smsApiKey, smsApiSecret, smsApiDomain);
    }
}
