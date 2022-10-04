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
public class AdminService {

    private final AdminOrderService adminOrderService;

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;
    private final KasService kasService;
    private final S3Service s3Service;

    @Transactional
    public void orderDetailToPaid(Long orderDetailId) {
        adminOrderService.orderDetailToPaid(orderDetailId);
    }

    @Transactional
    public void orderDetailToUnpaid(Long orderDetailId) {
        adminOrderService.orderDetailToUnpaid(orderDetailId);
    }

    @Transactional
    public void orderDetailToRefund(Long orderDetailId) {
        adminOrderService.orderDetailToRefund(orderDetailId);
    }

    @Transactional
    public void issueEvent(EventIssueRequest eventIssueRequest)
            throws InterruptedException {
        deployContract(eventIssueRequest);
        String contractAddress = getContractAddress(eventIssueRequest.getContractAlias());
        saveEvent(
            eventIssueRequest, contractAddress, eventIssueRequest.getContractName());
        log.info("$$ [ADMIN] DEPLOYED CONTRACT ADDRESS : " + contractAddress + " $$");
    }

    private void deployContract(EventIssueRequest eventIssueRequest)
            throws InterruptedException {
        String contractName = eventIssueRequest.getContractName();
        String contractSymbol = eventIssueRequest.getContractSymbol();
        String contractAlias = eventIssueRequest.getContractAlias();
        kasService.deployMyContract(contractName, contractSymbol, contractAlias);
        Thread.sleep(2000);
    }

    private String getContractAddress(String contractAlias) throws InterruptedException {
        String address = "updateme";
        while (address.equals("updateme")) {
            ContractItemResponse contractItemResponse =
                    kasService.getMyContractByAlias(contractAlias);
            address = contractItemResponse.getAddress();
            Thread.sleep(1000);
        }
        return address;
    }

    private void saveEvent(
            EventIssueRequest eventIssueRequest,
            String contractAddress,
            String contractName) {
        Artist eventArtist =
                artistRepository
                        .findById(eventIssueRequest.getEventArtistId())
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.ARTIST_NOT_FOUND));

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

    @Transactional
    public void issueTokens(TokenIssueRequest tokenIssueRequest) {
        int startTokenId = tokenIssueRequest.getStartTokenId();
        int endTokenId = tokenIssueRequest.getEndTokenId();
        String contractAddress = tokenIssueRequest.getContractAddress();
        String tokenUri = tokenIssueRequest.getTokenUri();
        int price = tokenIssueRequest.getPrice();

        Event event =
                eventRepository
                        .findByContractAddress(contractAddress)
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST, ErrorType.EVENT_NOT_FOUND));

        for (int tokenId = startTokenId; tokenId <= endTokenId; tokenId++) {
            mintToken(contractAddress, tokenUri, price, event, tokenId);
        }
    }

    private void mintToken(
            String contractAddress, String tokenUri, int price, Event event, int tokenId) {
        kasService.mintMyToken(contractAddress, tokenId, tokenUri);
        TicketMetadata ticketMetadata = s3Service.fetchMetadata(tokenUri).toTicketMetadata();
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
        log.info(
                "$$ [ADMIN] TOKEN MINTED AT ADDRESS : "
                        + contractAddress
                        + " - TOKEN ID : "
                        + tokenId
                        + " $$");
    }
}
