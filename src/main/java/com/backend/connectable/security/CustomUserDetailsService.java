package com.backend.connectable.security;

import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String klaytnAddress) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByKlaytnAddress(klaytnAddress);
        return optionalUser.map(ConnectableUserDetails::new)
                .orElseThrow(() -> new IllegalAccessError("로그인 실패!"));
    }
}
