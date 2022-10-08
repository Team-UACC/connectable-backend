package com.backend.connectable.security.custom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ConnectableUserDetailsTest {

    @DisplayName("ConnectableUserDetails를 통해 klaytnAddress 정보를 저장할 수 있다.")
    @Test
    void saveKlaytnAddress() {
        // given
        String klaytnAddress = "0x1234";

        // when
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(klaytnAddress);

        // then
        assertThat(connectableUserDetails.getKlaytnAddress()).isEqualTo(klaytnAddress);
    }

    @DisplayName(
            "ConnectableUserDetails가 구현한 UserDetails 인터페이스 메서드는 CustomUserDetail 추상 클래스의 구현을 따른다.")
    @Test
    void userDetailsFollowCustomUserDetails() {
        // given & when
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails("0x1234");
        TestCustomUserDetails testCustomUserDetails = new TestCustomUserDetails();

        // then
        assertThat(connectableUserDetails.getAuthorities())
                .isEqualTo(testCustomUserDetails.getAuthorities());
        assertThat(connectableUserDetails.getPassword())
                .isEqualTo(testCustomUserDetails.getPassword());
        assertThat(connectableUserDetails.getUsername())
                .isEqualTo(testCustomUserDetails.getUsername());
        assertThat(connectableUserDetails.isAccountNonExpired())
                .isEqualTo(testCustomUserDetails.isAccountNonExpired());
        assertThat(connectableUserDetails.isAccountNonLocked())
                .isEqualTo(testCustomUserDetails.isAccountNonLocked());
        assertThat(connectableUserDetails.isCredentialsNonExpired())
                .isEqualTo(testCustomUserDetails.isCredentialsNonExpired());
        assertThat(connectableUserDetails.isEnabled()).isEqualTo(testCustomUserDetails.isEnabled());
    }
}

class TestCustomUserDetails extends CustomUserDetails {}
