package com.backend.connectable.user.service;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.security.JwtProvider;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.backend.connectable.user.ui.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final KlipService klipService;
    private final JwtProvider jwtProvider;
    private final UserTicketService userTicketService;

    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        String requestKey = userLoginRequest.getRequestKey();
        KlipAuthLoginResponse klipAuthLoginResponse = klipService.authLogin(requestKey);
        if (klipAuthLoginResponse.isPrepared()) {
            return UserLoginResponse.ofPrepared();
        }
        if (klipAuthLoginResponse.isFailed()) {
            return UserLoginResponse.ofFailed();
        }
        return completeLogin(klipAuthLoginResponse.getKlaytnAddress());
    }

    private UserLoginSuccessResponse completeLogin(String klaytnAddress) {
        Optional<User> optionalUser =
                userRepository.findByKlaytnAddressAndIsActive(klaytnAddress, true);
        return optionalUser
                .map(user -> completeRegisteredUserLogin(klaytnAddress, user))
                .orElseGet(() -> completeNewUserLogin(klaytnAddress));
    }

    private UserLoginSuccessResponse completeRegisteredUserLogin(String klaytnAddress, User user) {
        if (user.hasNickname() && user.hasPhoneNumber()) {
            return UserLoginSuccessResponse.of(
                    user, jwtProvider.generateToken(klaytnAddress), false);
        }
        return UserLoginSuccessResponse.of(user, jwtProvider.generateToken(klaytnAddress), true);
    }

    private UserLoginSuccessResponse completeNewUserLogin(String klaytnAddress) {
        User user = User.builder().klaytnAddress(klaytnAddress).isActive(true).build();
        userRepository.save(user);
        return UserLoginSuccessResponse.of(user, jwtProvider.generateToken(klaytnAddress), true);
    }

    public UserResponse getUserByUserDetails(ConnectableUserDetails userDetails) {
        User user = findUser(userDetails.getKlaytnAddress());
        if (user.hasNickname() && user.hasPhoneNumber()) {
            return UserResponse.ofSuccess(user);
        }
        return UserResponse.ofFailure(user);
    }

    private User findUser(String klaytnAddress) {
        return userRepository
                .findByKlaytnAddress(klaytnAddress)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.USER_NOT_FOUND));
    }

    @Transactional
    public UserModifyResponse deleteUserByUserDetails(ConnectableUserDetails userDetails) {
        User user = findUser(userDetails.getKlaytnAddress());
        userRepository.deleteUser(user.getKlaytnAddress());
        return UserModifyResponse.ofSuccess();
    }

    @Transactional
    public UserModifyResponse modifyUserByUserDetails(
            ConnectableUserDetails userDetails, UserModifyRequest userModifyRequest) {
        User user = findUser(userDetails.getKlaytnAddress());
        user.modifyInformation(userModifyRequest.getNickname(), userModifyRequest.getPhoneNumber());
        return UserModifyResponse.ofSuccess();
    }

    public UserValidationResponse validateNickname(String nickname) {
        boolean isExistingNickname = userRepository.existsByNickname(nickname);
        if (isExistingNickname) {
            return UserValidationResponse.ofUnavailable();
        }
        return UserValidationResponse.ofAvailable();
    }

    public UserTicketListResponse getUserTicketsByUserDetails(ConnectableUserDetails userDetails) {
        return userTicketService.getUserTicketsByUserDetails(userDetails);
    }

    public UserTicketVerificationResponse generateUserTicketEntranceVerification(
            ConnectableUserDetails userDetails, Long ticketId) {
        User user = findUser(userDetails.getKlaytnAddress());
        return userTicketService.generateUserTicketEntranceVerification(user, ticketId);
    }

    @Transactional
    public UserTicketEntranceResponse useTicketToEnter(
            Long ticketId, UserTicketEntranceRequest userTicketEntranceRequest) {
        return userTicketService.useTicketToEnter(ticketId, userTicketEntranceRequest);
    }
}
