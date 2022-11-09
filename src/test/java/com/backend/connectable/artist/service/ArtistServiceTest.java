package com.backend.connectable.artist.service;

import static com.backend.connectable.fixture.ArtistFixture.*;
import static com.backend.connectable.fixture.EventFixture.createEventWithName;
import static com.backend.connectable.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.Comment;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.domain.repository.CommentRepository;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.artist.ui.dto.ArtistNftHolderResponse;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.List;
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
    @Autowired EventRepository eventRepository;
    @Autowired ArtistService artistService;
    @MockBean KasService kasService;

    private User user;
    private Artist artist1;
    private Artist artist2;

    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;

    private final String userKlaytnAddress = "0x1111";
    private final String anotherPersonKlaytnAddress = "0x2222";

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        commentRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user = createUserMrLee();
        artist1 = createArtistBigNaughty();
        artist2 = createArtistChoi();

        event1 = createEventWithName(artist1, "event1");
        event2 = createEventWithName(artist1, "event2");
        event3 = createEventWithName(artist1, "event3");
        event4 = createEventWithName(artist1, "event4");

        artistRepository.saveAll(List.of(artist1, artist2));
        userRepository.save(user);
        eventRepository.saveAll(List.of(event1, event2, event3, event4));
    }

    @DisplayName("아티스트를 전부 조회한다.")
    @Test
    void getAllArtists() {
        // given & when
        List<ArtistDetailResponse> artistDetailResponses = artistService.getAllArtists();

        // then
        assertThat(artistDetailResponses.size()).isEqualTo(2L);
        assertThat(artistDetailResponses.get(0).getArtistImage())
                .isEqualTo(artist1.getArtistImage());
        assertThat(artistDetailResponses.get(0).getArtistName()).isEqualTo(artist1.getArtistName());
        assertThat(artistDetailResponses.get(1).getArtistImage())
                .isEqualTo(artist2.getArtistImage());
        assertThat(artistDetailResponses.get(1).getArtistName()).isEqualTo(artist2.getArtistName());
    }

    @DisplayName("특정 아티스트의 상세 정보 조회에 성공한다.")
    @Test
    void getArtistDetail() {
        // given & when
        ArtistDetailResponse artistDetailResponse = artistService.getArtistDetail(artist1.getId());

        // then
        assertThat(artistDetailResponse.getArtistName()).isEqualTo(artist1.getArtistName());
        assertThat(artistDetailResponse.getArtistImage()).isEqualTo(artist1.getArtistImage());
    }

    @DisplayName("아티스트 페이지에서 방문록을 작성할 수 있다.")
    @Test
    void createComment() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(userKlaytnAddress);
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
        Comment comment1 =
                Comment.builder()
                        .user(user)
                        .artist(artist2)
                        .contents("contents1 입니당")
                        .isDeleted(true)
                        .build();
        Comment comment2 =
                Comment.builder()
                        .user(user)
                        .artist(artist2)
                        .contents("contents2 입니당")
                        .isDeleted(false)
                        .build();
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<ArtistCommentResponse> artistComments =
                artistService.getUndeletedArtistComments(artist2.getId());

        // then
        assertThat(artistComments.get(0).getContents()).isEqualTo(comment2.getContents());
        assertThat(artistComments.get(0).getWrittenAt()).isEqualTo(comment2.getCreatedDate());
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
                        event1.getEventName(),
                        event2.getEventName(),
                        event3.getEventName(),
                        event4.getEventName());
    }

    @DisplayName("등록된 코멘트를 삭제처리 진행할 경우 is_deleted=true가 된다.")
    @Test
    void deleteArtistComment() {
        // given
        ConnectableUserDetails connectableUserDetails =
                new ConnectableUserDetails(userKlaytnAddress);
        Comment comment =
                Comment.builder().user(user).artist(artist2).contents("contents1 입니당").build();
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
                Comment.builder().user(user).artist(artist2).contents("contents1 입니당").build();
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
                new ConnectableUserDetails(userKlaytnAddress);
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
                new ConnectableUserDetails(userKlaytnAddress);
        given(kasService.checkIsTokenHolder(any(), any())).willReturn(false);

        // when
        ArtistNftHolderResponse result =
                artistService.isArtistNftOwner(connectableUserDetails, artist1.getId());

        // then
        assertThat(result.getIsNftHolder()).isFalse();
    }
}
