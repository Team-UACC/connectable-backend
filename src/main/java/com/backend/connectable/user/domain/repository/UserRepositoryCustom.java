package com.backend.connectable.user.domain.repository;

import com.backend.connectable.user.domain.dto.UserTicket;

import java.util.List;

public interface UserRepositoryCustom {

    void deleteUser(String klaytnAddress);

    void modifyUser(String klaytnAddress, String nickname, String phoneNumber);

    List<UserTicket> getOwnTicketsByUser(Long userId);
}
