package com.backend.connectable.user.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByKlaytnAddress(String klaytnAddress);

    Optional<User> findByKlaytnAddressAndIsActive(String klaytnAddress, boolean isActive);
}
