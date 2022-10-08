package com.backend.connectable.auth.service;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.global.redis.RedisDao;
import com.backend.connectable.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisDao redisDao;
    private final SmsService smsService;

    public String getAuthKey(String phoneNumber, Long duration) {
        String generatedKey = generateCertificationKey();
        smsService.sendSignUpAuthKey(generatedKey, phoneNumber);
        redisDao.setDataExpire(phoneNumber, generatedKey, 60 * duration);
        return generatedKey;
    }

    public boolean certifyKey(String phoneNumber, String authKey) {
        String generatedKey = redisDao.getData(phoneNumber);
        return validateAuthKey(generatedKey, authKey);
    }

    private boolean validateAuthKey(String generatedKey, String authKey) {
        if (Objects.isNull(generatedKey) || !authKey.equals(generatedKey)) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_AUTH_KEY);
        }
        return true;
    }

    private String generateCertificationKey() {
        return RandomStringUtils.randomNumeric(6);
    }
}
