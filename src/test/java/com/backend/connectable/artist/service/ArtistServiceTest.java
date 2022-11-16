package com.backend.connectable.artist.service;

import static com.backend.connectable.fixture.ArtistFixture.*;
import static com.backend.connectable.fixture.EventFixture.createEventWithName;
import static com.backend.connectable.fixture.TicketFixture.createTicket;
import static com.backend.connectable.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.Comment;
import com.backend.connectable.artist.domain.Notice;
import com.backend.connectable.artist.domain.NoticeStatus;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.domain.repository.CommentRepository;
import com.backend.connectable.artist.domain.repository.NoticeRepository;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.artist.ui.dto.ArtistNftHolderResponse;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.token.dto.TokenIdentifier;
import com.backend.connectable.kas.service.token.dto.TokenIdentifierByKlaytnAddress;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ArtistServiceTest {

    @Autowired ArtistRepository artistRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired NoticeRepository noticeRepository;
    @Autowired TicketRepository ticketRepository;

    @Autowired EventRepository eventRepository;
    @Autowired ArtistService artistService;
    @MockBean KasService kasService;

    private User user1;
    private User user2;
    private Artist artist1;
    private Artist artist2;

    private Event event1ByArtist1;
    private Event event2ByArtist1;
    private Event event3ByArtist1;
    private Event event4ByArtist1;

    private Event event5ByArtist2;
    private Event event6ByArtist2;
    private Event event7ByArtist2;
    private Event event8ByArtist2;

    private String user1KlaytnAddress;
    private final String anotherPersonKlaytnAddress = "0x2222";

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        noticeRepository.deleteAll();
        eventRepository.deleteAll();
        commentRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user1 = createUserMrLee();
        user1KlaytnAddress = user1.getKlaytnAddress();
        user2 = createUserJoel();
        artist1 = createArtistBigNaughty();
        artist2 = createArtistChoi();

        event1ByArtist1 = createEventWithName(artist1, "event1");
        event2ByArtist1 = createEventWithName(artist1, "event2");
        event3ByArtist1 = createEventWithName(artist1, "event3");
        event4ByArtist1 = createEventWithName(artist1, "event4");

        event5ByArtist2 = createEventWithName(artist2, "event5");
        event6ByArtist2 = createEventWithName(artist2, "event6");
        event7ByArtist2 = createEventWithName(artist2, "event7");
        event8ByArtist2 = createEventWithName(artist2, "event8");

        artistRepository.saveAll(List.of(artist1, artist2));
        userRepository.saveAll(List.of(user1, user2));
        eventRepository.saveAll(
                List.of(
                        event1ByArtist1,
                        event2ByArtist1,
                        event3ByArtist1,
                        event4ByArtist1,
                        event5ByArtist2,
                        event6ByArtist2,
                        event7ByArtist2,
                        event8ByArtist2));
    }

    @DisplayName("아티스트를 전부 조회한다.")
    @Test
    void getAllArtists() {
        // given & when
        List<ArtistDetailResponse> artistDetailResponses = artistService.getAllArtists();

        // then
        assertThat(artistDetailResponses.size()).isEqualTo(2L);
        assertThat(artistDetailResponses.get(0).getImage()).isEqualTo(artist1.getArtistImage());
        assertThat(artistDetailResponses.get(0).getName()).isEqualTo(artist1.getArtistName());
        assertThat(artistDetailResponses.get(1).getImage()).isEqualTo(artist2.getArtistImage());
        assertThat(artistDetailResponses.get(1).getName()).isEqualTo(artist2.getArtistName());
    }

    @DisplayName("특정 아티스트의 상세 정보 조회에 성공한다.")
    @Test
    void getArtistDetail() {
        // given & when
        Notice notice =
                Notice.builder()
                        .artist(artist1)
                        .noticeStatus(NoticeStatus.EXPOSURE)
                        .title("공지사항1")
                        .contents("공지사항 1의 내용")
                        .build();
        noticeRepository.save(notice);
        ArtistDetailResponse artistDetailResponse = artistService.getArtistDetail(artist1.getId());

        // then
        assertThat(artistDetailResponse.getName()).isEqualTo(artist1.getArtistName());
        assertThat(artistDetailResponse.getImage()).isEqualTo(artist1.getArtistImage());
        assertThat(artistDetailResponse.getDescription()).isEqualTo(artist1.getDescription());
        assertThat(artistDetailResponse.getNotice().getTitle()).isEqualTo(notice.getTitle());
        assertThat(artistDetailResponse.getNotice().getContents()).isEqualTo(notice.getContents());
    }

    @DisplayName("아티스트 페이지에서 방문록을 작성할 수 있다.")
    @Test
    void createComment() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user1KlaytnAddress);
        ArtistCommentRequest artistCommentRequest =
                new ArtistCommentRequest("나는 내 갈길을 간다...! @_@ by mrlee7.");

        // when
        artistService.createComment(connectableUserDetails, artist1.getId(), artistCommentRequest);
        List<Comment> comments = commentRepository.findAll();

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.get(0).getContents()).isEqualTo(artistCommentRequest.getContents());
    }

    @DisplayName("특정 아티스트 페이지에 작성된 코멘트 목록을 조회할 수 있다.")
    @Test
    void getUndeletedArtistComments() {
        // given
        Comment comment1ByUser1 =
                Comment.builder()
                        .user(user1)
                        .artist(artist2)
                        .contents("contents1 입니당")
                        .isDeleted(true)
                        .build();

        Comment comment2ByUser1 =
                Comment.builder()
                        .user(user1)
                        .artist(artist2)
                        .contents("contents2 입니당")
                        .isDeleted(false)
                        .build();

        Comment comment3ByUser2 =
                Comment.builder()
                        .user(user2)
                        .artist(artist2)
                        .contents("contents3 입니당")
                        .isDeleted(false)
                        .build();

        Comment comment4ByUser2 =
                Comment.builder()
                        .user(user2)
                        .artist(artist2)
                        .contents("contents4 입니당")
                        .isDeleted(false)
                        .build();

        commentRepository.saveAll(
                List.of(comment1ByUser1, comment2ByUser1, comment3ByUser2, comment4ByUser2));

        Ticket ticket1 = createTicket(event5ByArtist2, 1, "https://event5ByArtist2");
        Ticket ticket2 = createTicket(event6ByArtist2, 1, "https://event6ByArtist2");
        ticketRepository.saveAll(List.of(ticket1, ticket2));

        Map<String, TokenIdentifier> tokenHolderStatus = new HashMap<>();
        tokenHolderStatus.put(
                user1.getKlaytnAddress(),
                new TokenIdentifier(String.valueOf(ticket1.getTokenId()), ticket1.getTokenUri()));
        tokenHolderStatus.put(
                user2.getKlaytnAddress(),
                new TokenIdentifier(String.valueOf(ticket2.getTokenId()), ticket2.getTokenUri()));

        given(kasService.findTokenHoldingStatus(any(), any()))
                .willReturn(new TokenIdentifierByKlaytnAddress(tokenHolderStatus));

        // when
        List<ArtistCommentResponse> artistComments =
                artistService.getUndeletedArtistComments(artist2.getId());

        // then
        assertThat(artistComments).hasSize(3);
        assertThat(artistComments.get(0).getContents()).isEqualTo("contents2 입니당");
        assertThat(artistComments.get(0).getTicketMetadata()).isNotNull();
        assertThat(artistComments.get(1).getContents()).isEqualTo("contents3 입니당");
        assertThat(artistComments.get(1).getTicketMetadata()).isNotNull();
        assertThat(artistComments.get(2).getContents()).isEqualTo("contents4 입니당");
        assertThat(artistComments.get(2).getTicketMetadata()).isNotNull();
    }

    @DisplayName("특정 아티스트 페이지에 작성된 코멘트 목록을 조회할 수 있으며, 홀더가 아니면 조회되지 않는다.")
    @Test
    void getUndeletedArtistCommentsOnlyHolder() {
        // given
        Comment comment1ByUser1 =
                Comment.builder()
                        .user(user1)
                        .artist(artist2)
                        .contents("contents1 입니당")
                        .isDeleted(true)
                        .build();

        Comment comment2ByUser1 =
                Comment.builder()
                        .user(user1)
                        .artist(artist2)
                        .contents("contents2 입니당")
                        .isDeleted(false)
                        .build();

        Comment comment3ByUser2 =
                Comment.builder()
                        .user(user2)
                        .artist(artist2)
                        .contents("contents3 입니당")
                        .isDeleted(false)
                        .build();

        Comment comment4ByUser2 =
                Comment.builder()
                        .user(user2)
                        .artist(artist2)
                        .contents("contents4 입니당")
                        .isDeleted(false)
                        .build();

        commentRepository.saveAll(
                List.of(comment1ByUser1, comment2ByUser1, comment3ByUser2, comment4ByUser2));

        Ticket ticket1 = createTicket(event5ByArtist2, 1, "https://event5ByArtist2");
        Ticket ticket2 = createTicket(event6ByArtist2, 1, "https://event6ByArtist2");
        ticketRepository.saveAll(List.of(ticket1, ticket2));

        Map<String, TokenIdentifier> tokenHolderStatus = new HashMap<>();
        tokenHolderStatus.put(
                user1.getKlaytnAddress(),
                new TokenIdentifier(String.valueOf(ticket1.getTokenId()), ticket1.getTokenUri()));

        given(kasService.findTokenHoldingStatus(any(), any()))
                .willReturn(new TokenIdentifierByKlaytnAddress(tokenHolderStatus));

        // when
        List<ArtistCommentResponse> artistComments =
                artistService.getUndeletedArtistComments(artist2.getId());

        // then
        assertThat(artistComments).hasSize(1);
        assertThat(artistComments.get(0).getContents()).isEqualTo("contents2 입니당");
        assertThat(artistComments.get(0).getTicketMetadata()).isNotNull();
    }

    @DisplayName("찾을 수 없는 유저일 경우에는 코멘트 등록에 실패한다.")
    @Test
    void getExceptionWhenNotExistedUserCreateComment() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails("0x0000");
        ArtistCommentRequest artistCommentRequest = new ArtistCommentRequest("unknown.");

        // when & then
        assertThatThrownBy(
                        () -> {
                            artistService.createComment(
                                    connectableUserDetails, artist1.getId(), artistCommentRequest);
                        })
                .isInstanceOf(ConnectableException.class)
                .withFailMessage(ErrorType.USER_NOT_FOUND.getMessage());
    }

    @DisplayName("찾을 수 없는 아티스트일 경우에는 아티스트 상세 조회에 실패한다.")
    @Test
    void getExceptionWhenNotExistedArtistGetArtistComments() {
        assertThatThrownBy(
                        () -> {
                            artistService.getArtistDetail(0L);
                        })
                .isInstanceOf(ConnectableException.class)
                .withFailMessage(ErrorType.ARTIST_NOT_EXISTS.getMessage());
    }

    @DisplayName("아티스트의 이벤트를 조회할 수 있다")
    @Test
    void getArtistEvent() {
        // given & when
        List<EventResponse> artist1Events = artistService.getArtistEvent(artist1.getId());

        // then
        assertThat(artist1Events)
                .extracting("name")
                .containsExactly(
                        event1ByArtist1.getEventName(),
                        event2ByArtist1.getEventName(),
                        event3ByArtist1.getEventName(),
                        event4ByArtist1.getEventName());
    }

    @DisplayName("등록된 코멘트를 삭제처리 진행할 경우 is_deleted=true가 된다.")
    @Test
    void deleteArtistComment() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user1KlaytnAddress);
        Comment comment =
                Comment.builder().user(user1).artist(artist2).contents("contents1 입니당").build();
        commentRepository.save(comment);

        // when
        artistService.deleteComment(connectableUserDetails, artist2.getId(), comment.getId());
        Optional<Comment> deletedComment = commentRepository.findById(comment.getId());

        // then
        assertThat(deletedComment.get().isDeleted()).isTrue();
    }

    @DisplayName("작성자가 아닌 타인이 열람된 코멘트를 삭제할 수 없다.")
    @Test
    void deleteArtistCommentByAnotherPerson() {
        // given
        ConnectableUserDetails anotherPersonUserDetails =
                new ConnectableUserDetails(anotherPersonKlaytnAddress);
        Comment comment =
                Comment.builder().user(user1).artist(artist2).contents("contents1 입니당").build();
        commentRepository.save(comment);

        // when & then
        assertThatThrownBy(
                        () ->
                                artistService.deleteComment(
                                        anotherPersonUserDetails, artist2.getId(), comment.getId()))
                .isInstanceOf(ConnectableException.class)
                .withFailMessage(ErrorType.NOT_A_COMMENT_AUTHOR.getMessage());
    }

    @DisplayName("NFT 홀더라면, isHolder=true를 반환한다")
    @Test
    void isArtistNftHolder() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user1KlaytnAddress);
        given(kasService.checkIsTokenHolder(any(), any())).willReturn(true);

        // when
        ArtistNftHolderResponse result =
                artistService.isArtistNftOwner(connectableUserDetails, artist1.getId());

        // then
        assertThat(result.getIsNftHolder()).isTrue();
    }

    @DisplayName("NFT 홀더가 아니라면, isHolder=false를 반환한다")
    @Test
    void isArtistNotNftHolder() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(user1KlaytnAddress);
        given(kasService.checkIsTokenHolder(any(), any())).willReturn(false);

        // when
        ArtistNftHolderResponse result =
                artistService.isArtistNftOwner(connectableUserDetails, artist1.getId());

        // then
        assertThat(result.getIsNftHolder()).isFalse();
    }
}
