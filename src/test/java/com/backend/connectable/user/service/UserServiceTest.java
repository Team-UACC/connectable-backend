package com.backend.connectable.user.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.event.service.EventService;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.backend.connectable.user.redis.UserTicketEntrance;
import com.backend.connectable.user.redis.UserTicketEntranceRedisRepository;
import com.backend.connectable.user.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    UserTicketEntranceRedisRepository userTicketEntranceRedisRepository;

    @Autowired
    EntityManager em;

    @MockBean
    KlipService klipService;

    @MockBean
    EventService eventService;

    @Value("${entrance.device-secret}")
    String deviceSecret;

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
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01012345678")
            .artistImage("https://image.url")
            .build();

        event1 = Event.builder()
            .description("조엘의 콘서트 at Connectable")
            .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
            .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
            .contractAddress("0x123456")
            .eventName("조엘의 콘서트")
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
            .name("조엘 콘서트 #1")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test1.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A6");
            }})
            .build();

        ticket1 = Ticket.builder()
            .event(event1)
            .tokenUri("https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/1.json")
            .price(100000)
            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
            .ticketMetadata(ticket1Metadata)
            .build();

        ticket2Metadata = TicketMetadata.builder()
            .name("조엘 콘서트 #2")
            .description("조엘의 콘서트 at Connectable")
            .image("https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
            .attributes(new HashMap<>(){{
                put("Background", "Yellow");
                put("Artist", "Joel");
                put("Seat", "A5");
            }})
            .build();

        ticket2 = Ticket.builder()
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

    @DisplayName("ConnectableUserDetails로 특정 사용자를 조회할 수 있다.")
    @Test
    void getUserByUserDetails() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1.getKlaytnAddress());

        // when
        UserResponse userResponse = userService.getUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userResponse.getStatus()).isEqualTo("success");
        assertThat(userResponse.getNickname()).isEqualTo(user1.getNickname());
        assertThat(userResponse.getKlaytnAddress()).isEqualTo(user1.getKlaytnAddress());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
    }

    @DisplayName("ConnectableUserDetails로 특정 사용자 회원탈퇴를 실행할 수 있다.")
    @Test
    void deleteUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1.getKlaytnAddress());

        // when
        UserModifyResponse userModifyResponse = userService.deleteUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
    }
    
    @DisplayName("ConnectableUserDetails로 특정 사용자 수정을 실행할 수 있다.")
    @Test
    void modifyUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1.getKlaytnAddress());
        UserModifyRequest userModifyRequest = new UserModifyRequest("mrlee7", "01085161399");

        // when
        UserModifyResponse userModifyResponse = userService.modifyUserByUserDetails(connectableUserDetails, userModifyRequest);

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
    }

    @DisplayName("Klip에서 로그인이 completed 되었고, 이미 가입된 회원이라면 completed, klaytnAddress, jwt, isNew=False 를 받게된다")
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

    @DisplayName("Klip에서 로그인이 completed 되었지만, 등록만 된 회원이라면 completed, klaytnAddress, jwt, isNew=True 를 받게된다")
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

    @DisplayName("Klip에서 로그인이 completed 되었고, 새로 가입한 회원이라면 completed, klaytnAddress, jwt, isNew=True 를 받게된다")
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


    @DisplayName("Klip에서 로그인이 prepared 라면, status=prepared 를 받게된다")
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

    @DisplayName("Klip에서 로그인이 prepared 라면, status=failed 를 받게된다")
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

    @DisplayName("ConnectableUserDetails로 특정 사용자의 티켓목록을 조회할 수 있다.")
    @Test
    void getUserTicketsByUserDetails() {
        // given
        given(eventService.findTicketByUserAddress(user1KlaytnAddress)).willReturn(Arrays.asList(ticket1, ticket2));
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1.getKlaytnAddress());

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

    @DisplayName("ValidateNickname을 통해 특정 사용자의 닉네임을 사용가능한지를 검증할 수 있다.")
    @Test
    void validateNickname() {
        // given & when
        UserValidationResponse unavailable = userService.validateNickname(user1Nickname);
        UserValidationResponse available = userService.validateNickname("available");

        // then
        assertThat(unavailable.getAvailable()).isFalse();
        assertThat(available.getAvailable()).isTrue();
    }

    @DisplayName("User가 입장을 위해 Ticket Verification을 받을 수 있다.")
    @Test
    void getUserTicketEntranceVerification() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1KlaytnAddress);
        Long ticketId = ticket1.getId();
        given(eventService.findTicketById(ticketId)).willReturn(ticket1);

        // when
        UserTicketVerificationResponse userTicketEntranceVerification = userService.generateUserTicketEntranceVerification(connectableUserDetails, ticketId);

        // then
        assertThat(userTicketEntranceVerification.getKlaytnAddress()).isEqualTo(user1KlaytnAddress);
        assertThat(userTicketEntranceVerification.getTicketId()).isEqualTo(ticketId);

        UserTicketEntrance userTicketEntrance = userTicketEntranceRedisRepository.findById(user1KlaytnAddress).get();
        assertThat(userTicketEntranceVerification.getKlaytnAddress()).isEqualTo(userTicketEntrance.getKlaytnAddress());
        assertThat(userTicketEntranceVerification.getTicketId()).isEqualTo(userTicketEntrance.getTicketId());
        assertThat(userTicketEntranceVerification.getVerification()).isEqualTo(userTicketEntrance.getVerification());
    }

    @DisplayName("사전에 생성된 QR 정보를 매칭하여 입장에 사용할 수 있다.")
    @Test
    void useTicketToEnter() {
        // given
        given(eventService.findTicketById(ticket1.getId())).willReturn(ticket1);

        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1KlaytnAddress);
        UserTicketVerificationResponse userTicketEntranceVerification = userService.generateUserTicketEntranceVerification(connectableUserDetails, ticket1.getId());

        UserTicketEntranceRequest userTicketEntranceRequest = new UserTicketEntranceRequest(
            userTicketEntranceVerification.getKlaytnAddress(),
            userTicketEntranceVerification.getVerification(),
            deviceSecret
        );

        // when
        UserTicketEntranceResponse userTicketEntranceResponse = userService.useTicketToEnter(ticket1.getId(), userTicketEntranceRequest);

        // then
        assertThat(userTicketEntranceResponse.getStatus()).isEqualTo("success");
        assertThat(ticket1.isUsed()).isTrue();
    }

    @DisplayName("사전에 생성된 QR 정보에서 device-secret이 다르면 예외가 발생한다.")
    @Test
    void useTicketToEnterInvalidDeviceSecret() {
        // given
        given(eventService.findTicketById(ticket1.getId())).willReturn(ticket1);

        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1KlaytnAddress);
        UserTicketVerificationResponse userTicketEntranceVerification = userService.generateUserTicketEntranceVerification(connectableUserDetails, ticket1.getId());

        UserTicketEntranceRequest userTicketEntranceRequest = new UserTicketEntranceRequest(
            userTicketEntranceVerification.getKlaytnAddress(),
            userTicketEntranceVerification.getVerification(),
            "invalid-device-secret"
        );

        assertThatThrownBy(() -> userService.useTicketToEnter(ticket1.getId(), userTicketEntranceRequest))
            .isInstanceOf(ConnectableException.class);
    }

    @DisplayName("사전에 QR이 생성되지 않았다면 예외가 발생한다.")
    @Test
    void useTicketToEnterInvalidKlaytnAddress() {
        // given
        given(eventService.findTicketById(ticket1.getId())).willReturn(ticket1);
        UserTicketEntranceRequest userTicketEntranceRequest = new UserTicketEntranceRequest(
            user1KlaytnAddress,
            "random-string",
            deviceSecret
        );

        assertThatThrownBy(() -> userService.useTicketToEnter(ticket1.getId(), userTicketEntranceRequest))
            .isInstanceOf(ConnectableException.class);
    }
}
