package com.backend.connectable.artist.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.connectable.artist.domain.NoticeStatus;
import com.backend.connectable.artist.service.ArtistService;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.artist.ui.dto.ArtistNftHolderResponse;
import com.backend.connectable.artist.ui.dto.NoticeResponse;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ArtistService artistService;

    private static final NoticeResponse NOTICE_1 =
            new NoticeResponse("타이틀1", "내용1", NoticeStatus.EXPOSURE);
    private static final NoticeResponse NOTICE_2 =
            new NoticeResponse("타이틀2", "내용2", NoticeStatus.EXPOSURE);
    private static final ArtistDetailResponse ARTIST_RESPONSE_1 =
            new ArtistDetailResponse(
                    1L, "artist1", "https://artist1.img", null, null, null, "아티스트1 설명", NOTICE_1);
    private static final ArtistDetailResponse ARTIST_RESPONSE_2 =
            new ArtistDetailResponse(
                    2L, "artist2", "https://artist2.img", null, null, null, "아티스트2 설명", NOTICE_2);

    private static final EventResponse EVENT_RESPONSE_1 =
            new EventResponse(
                    1L,
                    "test1",
                    "/connectable-events/image_0xtest.jpeg",
                    LocalDateTime.now(),
                    "description1",
                    LocalDateTime.now(),
                    LocalDateTime.now());

    private static final TicketMetadata SAMPLE_TICKET_METADATA =
            TicketMetadata.builder()
                    .name("메타데이터")
                    .description("메타데이터 at Connectable")
                    .image(
                            "https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test2.png")
                    .attributes(
                            new HashMap<>() {
                                {
                                    put("Background", "Yellow");
                                    put("Artist", "Joel");
                                    put("Seat", "A5");
                                }
                            })
                    .build();

    private static final ArtistCommentResponse ARTIST_COMMENT_RESPONSE_1 =
            new ArtistCommentResponse(
                    "nickname1", "contents1", LocalDateTime.now(), SAMPLE_TICKET_METADATA);
    private static final ArtistCommentResponse ARTIST_COMMENT_RESPONSE_2 =
            new ArtistCommentResponse(
                    "nickname2", "contents2", LocalDateTime.now(), SAMPLE_TICKET_METADATA);

    @DisplayName("모든 아티스트 조회에 성공한다.")
    @Test
    void getAllArtists() throws Exception {
        // given & when
        List<ArtistDetailResponse> artists = List.of(ARTIST_RESPONSE_1, ARTIST_RESPONSE_2);
        given(artistService.getAllArtists()).willReturn(artists);

        // then
        mockMvc.perform(get("/artists").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[0].id").value(ARTIST_RESPONSE_1.getId()))
                .andExpect(jsonPath("$[0].name").value(ARTIST_RESPONSE_1.getName()))
                .andExpect(jsonPath("$[1].id").value(ARTIST_RESPONSE_2.getId()))
                .andExpect(jsonPath("$[1].name").value(ARTIST_RESPONSE_2.getName()))
                .andDo(print());
    }

    @DisplayName("아티스트 상세 조회에 성공한다.")
    @Test
    void getArtist() throws Exception {
        // given & when
        given(artistService.getArtistDetail(ARTIST_RESPONSE_1.getId()))
                .willReturn(ARTIST_RESPONSE_1);

        // then
        mockMvc.perform(
                        get("/artists/{artist-id}", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ARTIST_RESPONSE_1.getId()))
                .andExpect(jsonPath("$.name").value(ARTIST_RESPONSE_1.getName()))
                .andExpect(
                        jsonPath("$.notice.title").value(ARTIST_RESPONSE_1.getNotice().getTitle()))
                .andExpect(
                        jsonPath("$.notice.contents")
                                .value(ARTIST_RESPONSE_1.getNotice().getContents()))
                .andDo(print());
    }

    @DisplayName("아티스트의 이벤트를 조회한다")
    @Test
    void getArtistEvent() throws Exception {
        // given & when
        given(artistService.getArtistEvent(ARTIST_RESPONSE_1.getId()))
                .willReturn(List.of(EVENT_RESPONSE_1));

        // then
        mockMvc.perform(
                        get("/artists/{artist-id}/events", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(EVENT_RESPONSE_1.getId()))
                .andExpect(jsonPath("$[0].name").value(EVENT_RESPONSE_1.getName()))
                .andExpect(jsonPath("$[0].image").value(EVENT_RESPONSE_1.getImage()))
                .andExpect(jsonPath("$[0].description").value(EVENT_RESPONSE_1.getDescription()))
                .andDo(print());
    }

    @DisplayName("특정 아티스트의 방명록에 작성된 코멘트들을 조회한다")
    @Test
    void getArtistComments() throws Exception {
        // given & when
        given(artistService.getUndeletedArtistComments(ARTIST_RESPONSE_1.getId()))
                .willReturn(List.of(ARTIST_COMMENT_RESPONSE_1, ARTIST_COMMENT_RESPONSE_2));

        // then
        mockMvc.perform(
                        get("/artists/{artist-id}/comments", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[0].nickname").value(ARTIST_COMMENT_RESPONSE_1.getNickname()))
                .andExpect(jsonPath("$[0].contents").value(ARTIST_COMMENT_RESPONSE_1.getContents()))
                .andExpect(
                        jsonPath("$[0].ticketMetadata.name")
                                .value(SAMPLE_TICKET_METADATA.getName()))
                .andExpect(
                        jsonPath("$[0].ticketMetadata.description")
                                .value(SAMPLE_TICKET_METADATA.getDescription()))
                .andExpect(jsonPath("$[1].nickname").value(ARTIST_COMMENT_RESPONSE_2.getNickname()))
                .andExpect(jsonPath("$[1].contents").value(ARTIST_COMMENT_RESPONSE_2.getContents()))
                .andExpect(
                        jsonPath("$[1].ticketMetadata.name")
                                .value(SAMPLE_TICKET_METADATA.getName()))
                .andExpect(
                        jsonPath("$[1].ticketMetadata.description")
                                .value(SAMPLE_TICKET_METADATA.getDescription()))
                .andDo(print());
    }

    @DisplayName("특정 아티스트의 방명록에 코멘트를 작성한다")
    @WithUserDetails("0x1111")
    @Test
    void createArtistComment() throws Exception {
        // given & when
        ArtistCommentRequest artistCommentRequest =
                new ArtistCommentRequest("나는 내 갈길을 간다...! @_@ by mrlee7.");
        String artistCommentAsJson = objectMapper.writeValueAsString(artistCommentRequest);

        doNothing().when(artistService).createComment(any(), any(), any());

        // then
        mockMvc.perform(
                        post("/artists/{artist-id}/comments", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON)
                                .content(artistCommentAsJson))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("작성한 코멘트를 삭제한다.")
    @WithUserDetails("0x1111")
    @Test
    void deleteArtistComment() throws Exception {
        // given & when
        doNothing().when(artistService).deleteComment(any(), anyLong(), anyLong());

        // then
        mockMvc.perform(
                        delete("/artists/{artist-id}/comments?commentId={comment-id}", 1L, 1L)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("아티스트의 NFT를 소유하고 있다면 isNftHolder=true를 반환한다")
    @WithUserDetails("0x1111")
    @Test
    void isArtistNftOwner() throws Exception {
        // given & when
        given(artistService.isArtistNftOwner(any(), any()))
                .willReturn(ArtistNftHolderResponse.isHolder());

        // then
        mockMvc.perform(
                        get("/artists/{artist-id}/owner", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isNftHolder").value(true))
                .andDo(print());
    }

    @DisplayName("아티스트의 NFT를 소유하고 있다면 isNftHolder=false를 반환한다")
    @WithUserDetails("0x1111")
    @Test
    void isArtistNotNftOwner() throws Exception {
        // given & when
        given(artistService.isArtistNftOwner(any(), any()))
                .willReturn(ArtistNftHolderResponse.isNotHolder());

        // then
        mockMvc.perform(
                        get("/artists/{artist-id}/owner", ARTIST_RESPONSE_1.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isNftHolder").value(false))
                .andDo(print());
    }
}
