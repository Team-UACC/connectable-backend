package com.backend.connectable.s3.service;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.ui.dto.TicketMetadataResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3ServiceTest {

    @Autowired
    S3Service s3Service;

    @DisplayName("Url을 통해 nft의 메타데이터를 가져올 수 있다.")
    @Test
    void getMetadata() {
        // given
        String brownNft1TokenUri = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/1.json";
        String brownNft1Image = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/images/1.png";

        // when
        TicketMetadataResponse ticketMetadataResponse = s3Service.fetchMetadata(brownNft1TokenUri);
        TicketMetadata ticketMetadata = ticketMetadataResponse.toTicketMetadata();

        // then
        assertThat(ticketMetadata.getName()).isEqualTo("브라운 콘서트 #1");
        assertThat(ticketMetadata.getDescription()).isEqualTo("브라운 콘서트 at Connectable");
        assertThat(ticketMetadata.getImage()).isEqualTo(brownNft1Image);
        assertThat(ticketMetadata.getAttributes().get(0).getTrait_type()).isEqualTo("Artist");
        assertThat(ticketMetadata.getAttributes().get(0).getValue()).isEqualTo("brown");
        assertThat(ticketMetadata.getAttributes().get(1).getTrait_type()).isEqualTo("Seat");
        assertThat(ticketMetadata.getAttributes().get(1).getValue()).isEqualTo("A8");
    }
}
