package com.backend.connectable.sms.service;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private DefaultMessageService defaultMessageService;
    private static final String smsApiDomain = "https://api.coolsms.co.kr";

    @Value("${sms.source-phone-number}")
    private String sourcePhoneNumber;

    @Value("${sms.api-key}")
    private String smsApiKey;

    @Value("${sms.api-secret}")
    private String smsApiSecret;

    @PostConstruct
    private void initialize() {
        this.defaultMessageService =
                NurigoApp.INSTANCE.initialize(smsApiKey, smsApiSecret, smsApiDomain);
    }

    public void sendPaidNotification(String phoneNumber) {
        sendSms(MessageContent.getTicketOrderSuccess(), phoneNumber);
    }

    public void sendSignUpAuthKey(String generatedKey, String phoneNumber) {
        sendSms(MessageContent.getSignUpAuthRequestMessage(generatedKey), phoneNumber);
    }

    private void sendSms(String content, String target) {
        Message message = new Message();
        message.setFrom(sourcePhoneNumber);
        message.setTo(target.replace("-", ""));
        message.setText(content);
        SingleMessageSendingRequest singleMessageSendingRequest =
                new SingleMessageSendingRequest(message);
        try {
            defaultMessageService.sendOne(singleMessageSendingRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConnectableException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.NURIGO_EXCEPTION);
        }
    }
}
