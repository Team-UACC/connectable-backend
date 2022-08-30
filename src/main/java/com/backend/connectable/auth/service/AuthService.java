package com.backend.connectable.auth.service;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private DefaultMessageService defaultMessageService;
    private final RedisUtil redisUtil;

    @Value("${sms.source-phone-number}")
    private String sourcePhoneNumber;

    @Value("${sms.api-key}")
    private String smsApiKey;

    @Value("${sms.api-secret}")
    private String smsApiSecret;

    private static final String smsApiDomain = "https://api.coolsms.co.kr";

    public String getAuthKey(String phoneNumber, Long duration) {
        String generatedKey = generateCertificationKey();
        String message = "Connectable 회원가입을 위한 인증번호는 " + generatedKey + "입니다.";
        sendSms(message, phoneNumber);
        redisUtil.setDataExpire(phoneNumber, generatedKey, 60 * duration);
        return generatedKey;
    }

    public boolean certifyKey(String phoneNumber, String authKey) {
        String generatedKey = redisUtil.getData(phoneNumber);
        return validateAuthKey(generatedKey, authKey);
    }

    private boolean validateAuthKey(String generatedKey, String authKey) {
        if (Objects.isNull(generatedKey) || !authKey.equals(generatedKey)) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_AUTH_KEY);
        }
        return true;
    }

    private void sendSms(String content, String target) {
        defaultMessageService = NurigoApp.INSTANCE.initialize(smsApiKey, smsApiSecret, smsApiDomain);
        Message message = new Message();
        message.setFrom(sourcePhoneNumber);
        message.setTo(target.replace("-", ""));
        message.setText(content);
        SingleMessageSendingRequest singleMessageSendingRequest = new SingleMessageSendingRequest(message);
        defaultMessageService.sendOne(singleMessageSendingRequest);
    }

    private String generateCertificationKey() {
        return RandomStringUtils.randomNumeric(6);
    }
}
