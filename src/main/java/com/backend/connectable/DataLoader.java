package com.backend.connectable;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.s3.service.S3Service;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Profile("dev")
@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    @Value("${dataloader.enable}")
    private boolean enableDataloader;

    private final KasService kasService;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        if (enableDataloader) {
            runDataLoader();
        }
    }

    private void runDataLoader() throws InterruptedException {
        User admin = userRepository.findById(1L).get();
        Artist bigNaughty = artistRepository.findById(1L).get();

        String name = "Brown At Connectable";
        String symbol = "BAC";
        String alias = "bac";
        kasService.deployMyContract(name, symbol, alias);
        Thread.sleep(3000);

        ContractItemResponse contractItem = kasService.getMyContractMyAlias(alias);
        String contractAddress = contractItem.getAddress();
        log.info("$$ DEPLOYED CONTRACT ADDRESS : " + contractAddress + " $$");

        Event brownEvent =
                Event.builder()
                        .description("브라운 콘서트 at Connectable")
                        .salesFrom(LocalDateTime.of(2022, 8, 3, 0, 0))
                        .salesTo(LocalDateTime.of(2022, 10, 17, 0, 0))
                        .contractAddress(contractAddress)
                        .eventName("브라운 콘서트")
                        .eventImage(
                                "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown.jpeg")
                        .twitterUrl("https://twitter.com/elonmusk")
                        .instagramUrl("https://www.instagram.com/eunbining0904/")
                        .webpageUrl("https://nextjs.org/")
                        .startTime(LocalDateTime.of(2022, 10, 17, 19, 30))
                        .endTime(LocalDateTime.of(2022, 10, 17, 21, 30))
                        .salesOption(SalesOption.FLAT_PRICE)
                        .location("예술의 전당")
                        .artist(bigNaughty)
                        .build();
        eventRepository.save(brownEvent);

        int totalTickets = 30;
        for (int i = 1; i <= totalTickets; i++) {
            String tokenUri =
                    "https://connectable-events.s3.ap-northeast-2.amazonaws.com/brown-event/json/"
                            + i
                            + ".json";
            kasService.mintMyToken(contractAddress, i, tokenUri);
            TicketMetadata ticketMetadata = s3Service.fetchMetadata(tokenUri).toTicketMetadata();
            Ticket brownTicket =
                    Ticket.builder()
                            .event(brownEvent)
                            .tokenUri(tokenUri)
                            .tokenId(i)
                            .price(100000)
                            .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                            .ticketMetadata(ticketMetadata)
                            .build();
            ticketRepository.save(brownTicket);
        }
    }
}
