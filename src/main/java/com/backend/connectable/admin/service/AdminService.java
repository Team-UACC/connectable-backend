package com.backend.connectable.admin.service;

import com.backend.connectable.admin.ui.dto.EventDeploymentRequest;
import com.backend.connectable.admin.ui.dto.TokenMintingRequest;
import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.*;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.exception.KasException;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.common.dto.TransactionResponse;
import com.backend.connectable.kas.service.contract.dto.ContractItemResponse;
import com.backend.connectable.order.domain.OrderDetail;
import com.backend.connectable.order.domain.repository.OrderDetailRepository;
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

    private final OrderDetailRepository orderDetailRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final ArtistRepository artistRepository;
    private final KasService kasService;
    private final S3Service s3Service;

    @Transactional
    public void orderDetailToPaid(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.paid();
        sendTicket(orderDetail);
    }

    private OrderDetail findOrderDetail(Long orderDetailId) {
        return orderDetailRepository
                .findById(orderDetailId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.ORDER_DETAIL_NOT_EXISTS));
    }

    private void sendTicket(OrderDetail orderDetail) {
        String contractAddress = orderDetail.getContractAddress();
        int tokenId = orderDetail.getTokenId();
        String receiverAddress = orderDetail.getKlaytnAddress();
        try {
            TransactionResponse transactionResponse =
                    kasService.sendMyToken(contractAddress, tokenId, receiverAddress);
            orderDetail.transferSuccess(transactionResponse.getTransactionHash());
        } catch (KasException kasException) {
            orderDetail.transferFail();
        }
    }

    @Transactional
    public void orderDetailToUnpaid(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.unpaid();
    }

    @Transactional
    public void orderDetailToRefund(Long orderDetailId) {
        OrderDetail orderDetail = findOrderDetail(orderDetailId);
        orderDetail.refund();
    }

    @Transactional
    public void deployEvent(EventDeploymentRequest eventDeploymentRequest)
            throws InterruptedException {
        deployContract(eventDeploymentRequest);
        String contractAddress = getContractAddress(eventDeploymentRequest.getContractAlias());
        saveEvent(
                eventDeploymentRequest, contractAddress, eventDeploymentRequest.getContractName());
        log.info("$$ [ADMIN] DEPLOYED CONTRACT ADDRESS : " + contractAddress + " $$");
    }

    private void deployContract(EventDeploymentRequest eventDeploymentRequest)
            throws InterruptedException {
        String contractName = eventDeploymentRequest.getContractName();
        String contractSymbol = eventDeploymentRequest.getContractSymbol();
        String contractAlias = eventDeploymentRequest.getContractAlias();
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
            EventDeploymentRequest eventDeploymentRequest,
            String contractAddress,
            String contractName) {
        Artist eventArtist =
                artistRepository
                        .findById(eventDeploymentRequest.getEventArtistId())
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.ARTIST_NOT_FOUND));

        Event event =
                Event.builder()
                        .description(eventDeploymentRequest.getEventDescription())
                        .salesFrom(eventDeploymentRequest.getEventSalesFrom())
                        .salesTo(eventDeploymentRequest.getEventSalesTo())
                        .contractAddress(contractAddress)
                        .contractName(contractName)
                        .eventName(eventDeploymentRequest.getEventName())
                        .eventImage(eventDeploymentRequest.getEventImage())
                        .twitterUrl(eventDeploymentRequest.getEventTwitterUrl())
                        .instagramUrl(eventDeploymentRequest.getEventInstagramUrl())
                        .webpageUrl(eventDeploymentRequest.getEventWebpageUrl())
                        .startTime(eventDeploymentRequest.getEventStartTime())
                        .endTime(eventDeploymentRequest.getEventEndTime())
                        .salesOption(SalesOption.FLAT_PRICE)
                        .location(eventDeploymentRequest.getEventLocation())
                        .artist(eventArtist)
                        .build();
        eventRepository.save(event);
    }

    @Transactional
    public void mintTokens(TokenMintingRequest tokenMintingRequest) {
        int startTokenId = tokenMintingRequest.getStartTokenId();
        int endTokenId = tokenMintingRequest.getEndTokenId();
        String contractAddress = tokenMintingRequest.getContractAddress();
        String tokenUri = tokenMintingRequest.getTokenUri();
        int price = tokenMintingRequest.getPrice();

        Event event =
                eventRepository
                        .findByContractAddress(contractAddress)
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST, ErrorType.EVENT_NOT_FOUND));

        for (int tokenId = startTokenId; tokenId <= endTokenId; tokenId++) {
            mintAndSave(contractAddress, tokenUri, price, event, tokenId);
        }
    }

    private void mintAndSave(
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
