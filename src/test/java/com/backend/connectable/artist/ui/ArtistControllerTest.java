package com.backend.connectable.artist.ui;

import static com.backend.connectable.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.domain.repository.CommentRepository;
import com.backend.connectable.artist.service.ArtistService;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    @MockBean ArtistService artistService;

    @Autowired ArtistRepository artistRepository;
    @Autowired UserRepository userRepository;
    @Autowired CommentRepository commentRepository;

    private static final Long ARTIST1_ID = 1L;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User mrLee = createUserMrLee();
        userRepository.save(mrLee);
    }

    @DisplayName("모든 아티스트 조회에 성공한다.")
    @Test
    void getAllArtists() throws Exception {
        mockMvc.perform(get("/artists").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("아티스트 상세조회에 성공한다.")
    @Test
    void getArtist() throws Exception {
        mockMvc.perform(get("/artists/{artist-id}", ARTIST1_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("특정 아티스트의 방명록에 작성된 코멘트들을 조회한다")
    @Test
    void getArtistComments() throws Exception {
        mockMvc.perform(
                        get("/artists/{artist-id}/comments", ARTIST1_ID)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("특정 아티스트의 방명록에 코멘트를 작성한다")
    @WithUserDetails("0x1111")
    @Test
    void createArtistComment() throws Exception {
        ArtistCommentRequest artistCommentRequest =
                new ArtistCommentRequest("나는 내 갈길을 간다...! @_@ by mrlee7.");
        String json = objectMapper.writeValueAsString(artistCommentRequest);

        mockMvc.perform(
                        post("/artists/{artist-id}/comments", ARTIST1_ID)
                                .contentType(APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
