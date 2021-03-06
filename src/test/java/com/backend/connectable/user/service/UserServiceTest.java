package com.backend.connectable.user.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.backend.connectable.user.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    @MockBean
    KlipService klipService;

    private User user1;
    private Artist artist1;
    private Event event1;
    private TicketMetadata ticket1Metadata;
    private Ticket ticket1;
    private TicketMetadata ticket2Metadata;
    private Ticket ticket2;

    private final String user1KlaytnAddress = "0x1234";
    private final String user1Nickname = "Joel";
    private final String user1PhoneNumber = "010-1234-5678";
    private final boolean user1PrivacyAgreement = true;
    private final boolean user1IsActive = true;

    private final String noNicknameUserKlaytnAddress = "0x8876";

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user1 = User.builder()
                .klaytnAddress(user1KlaytnAddress)
                .nickname(user1Nickname)
                .phoneNumber(user1PhoneNumber)
                .privacyAgreement(user1PrivacyAgreement)
                .isActive(user1IsActive)
                .build();

        artist1 = Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("?????????")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage("https://image.url")
            .build();

        event1 = Event.builder()
            .description("????????? ????????? at Connectable")
            .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
            .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
            .contractAddress("0x123456")
            .eventName("????????? ?????????")
            .eventImage("https://image.url")
            .twitterUrl("https://github.com/joelonsw")
            .instagramUrl("https://www.instagram.com/jyoung_with/")
            .webpageUrl("https://papimon.tistory.com/")
            .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
            .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
            .eventSalesOption(EventSalesOption.FLAT_PRICE)
            .artist(artist1)
            .build();

        ticket1Metadata = TicketMetadata.builder()
            .name("?????? ????????? #1")
            .description("????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test1.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A6");
            }})
            .build();

        ticket1 = Ticket.builder()
            .user(user1)
            .event(event1)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ticket1Metadata)
            .build();

        ticket2Metadata = TicketMetadata.builder()
            .name("?????? ????????? #2")
            .description("????????? ????????? at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A5");
            }})
            .build();

        ticket2 = Ticket.builder()
            .user(user1)
            .event(event1)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.SOLD_OUT)
            .ticketMetadata(ticket2Metadata)
            .build();

        userRepository.save(user1);
        artistRepository.save(artist1);
        eventRepository.save(event1);
        ticketRepository.saveAll(Arrays.asList(ticket1, ticket2));
    }

    @DisplayName("ConnectableUserDetails??? ?????? ???????????? ????????? ??? ??????.")
    @Test
    void getUserByUserDetails() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);

        // when
        UserResponse userResponse = userService.getUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userResponse.getStatus()).isEqualTo("success");
        assertThat(userResponse.getNickname()).isEqualTo(user1.getNickname());
        assertThat(userResponse.getKlaytnAddress()).isEqualTo(user1.getKlaytnAddress());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
    }

    @DisplayName("ConnectableUserDetails??? ?????? ????????? ??????????????? ????????? ??? ??????.")
    @Test
    void deleteUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);

        // when
        UserModifyResponse userModifyResponse = userService.deleteUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
    }
    
    @DisplayName("ConnectableUserDetails??? ?????? ????????? ????????? ????????? ??? ??????.")
    @Test
    void modifyUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);
        UserModifyRequest userModifyRequest = new UserModifyRequest("mrlee7", "01085161399");

        // when
        UserModifyResponse userModifyResponse = userService.modifyUserByUserDetails(connectableUserDetails, userModifyRequest);

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
    }

    @DisplayName("Klip?????? ???????????? completed ?????????, ?????? ????????? ??????????????? completed, klaytnAddress, jwt, isNew=False ??? ????????????")
    @Test
    void loginExistingUser() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(user1KlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(user1KlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isFalse();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }

    @DisplayName("Klip?????? ???????????? completed ????????????, ????????? ??? ??????????????? completed, klaytnAddress, jwt, isNew=True ??? ????????????")
    @Test
    void loginOnlyRegisteredUser() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(noNicknameUserKlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(noNicknameUserKlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isTrue();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }

    @DisplayName("Klip?????? ???????????? completed ?????????, ?????? ????????? ??????????????? completed, klaytnAddress, jwt, isNew=True ??? ????????????")
    @Test
    void loginNewUser() {
        // given
        String newKlaytnAddress = "0x9dsf8xc12x9c0v";
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(newKlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(newKlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isTrue();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }


    @DisplayName("Klip?????? ???????????? prepared ??????, status=prepared ??? ????????????")
    @Test
    void loginPrepared() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofPrepared();
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));

        // then
        assertThat(userLoginResponse.getStatus()).isEqualTo("prepared");
    }

    @DisplayName("Klip?????? ???????????? prepared ??????, status=failed ??? ????????????")
    @Test
    void loginFailed() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofFailed();
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));

        // then
        assertThat(userLoginResponse.getStatus()).isEqualTo("failed");
    }

    @DisplayName("ConnectableUserDetails??? ?????? ???????????? ??????????????? ????????? ??? ??????.")
    @Test
    void getUserTicketsByUserDetails() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);

        // when
        UserTicketListResponse userTicketListResponse = userService.getUserTicketsByUserDetails(connectableUserDetails);

        // then
        assertEquals("success", userTicketListResponse.getStatus());
        assertEquals(2L, userTicketListResponse.getTickets().size());
        assertThat(userTicketListResponse.getTickets().get(0).getContractAddress()).isEqualTo("0x123456");
        assertThat(userTicketListResponse.getTickets().get(0).getTokenUri())
            .isEqualTo("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json");
        assertThat(userTicketListResponse.getTickets().get(1).getTokenUri())
            .isEqualTo("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/2.json");
    }

    @DisplayName("ValidateNickname??? ?????? ?????? ???????????? ???????????? ????????????????????? ????????? ??? ??????.")
    @Test
    void validateNickname() {
        // given & when
        UserValidationResponse unavailable = userService.validateNickname(user1Nickname);
        UserValidationResponse available = userService.validateNickname("available");

        // then
        assertThat(unavailable.getAvailable()).isFalse();
        assertThat(available.getAvailable()).isTrue();
    }
}
