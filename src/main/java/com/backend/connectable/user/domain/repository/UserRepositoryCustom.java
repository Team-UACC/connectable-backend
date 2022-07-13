package com.backend.connectable.user.domain.repository;

public interface UserRepositoryCustom {

    void deleteUser(String klaytnAddress);

    void modifyUser(String klaytnAddress, String nickname, String phoneNumber);
}
