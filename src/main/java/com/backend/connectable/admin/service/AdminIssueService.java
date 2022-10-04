package com.backend.connectable.admin.service;

import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.admin.ui.dto.TokenIssueRequest;
import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.global.common.util.WaitUtil;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminIssueService {

    private static final String PENDING_CONTRACT_ADDRESS = "updateme";

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;
    private final KasService kasService;
    private final S3Service s3Service;

    @Transactional
    public void issueEvent(EventIssueRequest eventIssueRequest) {
        deployContract(eventIssueRequest);
        String contractAddress = getContractAddress(eventIssueRequest.getContractAlias());
        saveEvent(eventIssueRequest, contractAddress, eventIssueRequest.getContractName());
        log.info("$$ [ADMIN] DEPLOYED CONTRACT ADDRESS : " + contractAddress + " $$");
    }

    private void deployContract(EventIssueRequest eventIssueRequest) {
        String contractName = eventIssueRequest.getContractName();
        String contractSymbol = eventIssueRequest.getContractSymbol();
        String contractAlias = eventIssueRequest.getContractAlias();
        kasService.deployMyContract(contractName, contractSymbol, contractAlias);
        WaitUtil.waitForSecond(2);
    }

    private String getContractAddress(String contractAlias) {
        String address = PENDING_CONTRACT_ADDRESS;
        while (address.equals(PENDING_CONTRACT_ADDRESS)) {
            ContractItemResponse contractItemResponse =
                    kasService.getMyContractByAlias(contractAlias);
            address = contractItemResponse.getAddress();
            WaitUtil.waitForSecond(1);
        }
        return address;
    }

    private void saveEvent(
            EventIssueRequest eventIssueRequest, String contractAddress, String contractName) {
        Artist eventArtist = findArtist(eventIssueRequest.getEventArtistId());

        Event event =
                Event.builder()
                        .description(eventIssueRequest.getEventDescription())
                        .salesFrom(eventIssueRequest.getEventSalesFrom())
                        .salesTo(eventIssueRequest.getEventSalesTo())
                        .contractAddress(contractAddress)
                        .contractName(contractName)
                        .eventName(eventIssueRequest.getEventName())
                        .eventImage(eventIssueRequest.getEventImage())
                        .twitterUrl(eventIssueRequest.getEventTwitterUrl())
                        .instagramUrl(eventIssueRequest.getEventInstagramUrl())
                        .webpageUrl(eventIssueRequest.getEventWebpageUrl())
                        .startTime(eventIssueRequest.getEventStartTime())
                        .endTime(eventIssueRequest.getEventEndTime())
                        .salesOption(SalesOption.FLAT_PRICE)
                        .location(eventIssueRequest.getEventLocation())
                        .artist(eventArtist)
                        .build();
        eventRepository.save(event);
    }

    private Artist findArtist(Long artistId) {
        return artistRepository
                .findById(artistId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.ARTIST_NOT_FOUND));
    }

    @Transactional
    public void issueTokens(TokenIssueRequest tokenIssueRequest) {
        int startTokenId = tokenIssueRequest.getStartTokenId();
        int endTokenId = tokenIssueRequest.getEndTokenId();
        String contractAddress = tokenIssueRequest.getContractAddress();
        String tokenUri = tokenIssueRequest.getTokenUri();
        int price = tokenIssueRequest.getPrice();

        Event event = findEvent(contractAddress);
        int tokenId = startTokenId;
        while (tokenId <= endTokenId) {
            mintToken(contractAddress, tokenUri, price, event, tokenId);
            tokenId++;
        }
    }

    private Event findEvent(String contractAddress) {
        return eventRepository
                .findByContractAddress(contractAddress)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.EVENT_NOT_FOUND));
    }

    private void mintToken(
            String contractAddress, String tokenUri, int price, Event event, int tokenId) {
        kasService.mintMyToken(contractAddress, tokenId, tokenUri);
        TicketMetadata ticketMetadata = s3Service.fetchMetadata(tokenUri).toTicketMetadata();
        saveTicket(event, tokenUri, tokenId, price, ticketMetadata);
        log.info(
                "$$ [ADMIN] TOKEN MINTED AT ADDRESS : "
                        + contractAddress
                        + " - TOKEN ID : "
                        + tokenId
                        + " $$");
    }

    private void saveTicket(
            Event event, String tokenUri, int tokenId, int price, TicketMetadata ticketMetadata) {
        Ticket ticket =
                Ticket.builder()
                        .event(event)
                        .tokenUri(tokenUri)
                        .tokenId(tokenId)
                        .price(price)
                        .ticketSalesStatus(TicketSalesStatus.ON_SALE)
                        .ticketMetadata(ticketMetadata)
                        .build();
        ticketRepository.save(ticket);
    }
}
