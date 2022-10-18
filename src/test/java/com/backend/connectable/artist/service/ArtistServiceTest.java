package com.backend.connectable.artist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.Comment;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.domain.repository.CommentRepository;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ArtistServiceTest {

    @Autowired ArtistRepository artistRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;

    @Autowired ArtistService artistService;

    private User user;
    private Artist artist1;
    private Artist artist2;

    private final String userKlaytnAddress = "0x12345678";
    private final String userNickname = "leejp";
    private final String userPhoneNumber = "010-3333-7777";
    private final boolean userPrivacyAgreement = true;
    private final boolean userIsActive = true;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        user =
                User.builder()
                        .klaytnAddress(userKlaytnAddress)
                        .nickname(userNickname)
                        .phoneNumber(userPhoneNumber)
                        .privacyAgreement(userPrivacyAgreement)
                        .isActive(userIsActive)
                        .build();

        artist1 =
                Artist.builder()
                        .bankCompany("NH")
                        .bankAccount("9000000000099")
                        .artistName("빅나티")
                        .email("bignaughty@gmail.com")
                        .password("temptemp1234")
                        .phoneNumber("01012345678")
                        .artistImage("https://image.url")
                        .build();

        artist2 =
                Artist.builder()
                        .bankCompany("TOSS")
                        .bankAccount("7000000000077")
                        .artistName("최유리")
                        .email("choi777@naver.com")
                        .password("temptemp1234")
                        .phoneNumber("01033339999")
                        .artistImage("https://image2.url")
                        .build();

        artistRepository.saveAll(List.of(artist1, artist2));
        userRepository.save(user);
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
    void getArtistComments() {
        // given
        Comment comment1 =
                Comment.builder().user(user).artist(artist2).contents("contents1 입니당").build();
        Comment comment2 =
                Comment.builder().user(user).artist(artist2).contents("contents2 입니당").build();
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<ArtistCommentResponse> artistComments =
                artistService.getArtistComments(artist2.getId());

        // then
        assertThat(artistComments.get(0).getContents()).isEqualTo(comment1.getContents());
        assertThat(artistComments.get(0).getWrittenAt()).isEqualTo(comment1.getCreatedDate());
        assertThat(artistComments.get(1).getContents()).isEqualTo(comment2.getContents());
        assertThat(artistComments.get(1).getWrittenAt()).isEqualTo(comment2.getCreatedDate());
    }

    // ToDo 찾을 수 없는 유저 및 아티스트에 대해 예외처리 테스트 필요
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
}
