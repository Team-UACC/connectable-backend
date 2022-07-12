package com.backend.connectable.user.domain.repository;


import com.backend.connectable.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByKlaytnAddress(String klaytnAddress);

    Optional<User> findByKlaytnAddressAndIsActive(String klaytnAddress, boolean isActive);
}
