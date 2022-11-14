package com.backend.connectable.artist.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.Comment;
import com.backend.connectable.artist.domain.dto.ArtistComment;
import com.backend.connectable.artist.domain.dto.ArtistDetail;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.domain.repository.CommentRepository;
import com.backend.connectable.artist.mapper.ArtistMapper;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.artist.ui.dto.ArtistNftHolderResponse;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.repository.EventRepository;
import com.backend.connectable.event.domain.repository.TicketRepository;
import com.backend.connectable.event.mapper.EventMapper;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.backend.connectable.global.aop.TimeCheck;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.token.dto.TokenIdentifier;
import com.backend.connectable.kas.service.token.dto.TokenIdentifierByKlaytnAddress;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {

    private final KasService kasService;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public List<ArtistDetailResponse> getAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        return ArtistDetailResponse.toList(artists);
    }

    public ArtistDetailResponse getArtistDetail(Long artistId) {
        ArtistDetail artistDetail =
                artistRepository
                        .findArtistDetailByArtistId(artistId)
                        .orElseThrow(
                                () ->
                                        new ConnectableException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.ARTIST_NOT_EXISTS));
        return ArtistMapper.INSTANCE.artistDetailToResponse(artistDetail);
    }

    public List<EventResponse> getArtistEvent(Long artistId) {
        List<Event> artistEvents = eventRepository.findAllEventsByArtistId(artistId);
        return artistEvents.stream()
                .map(EventMapper.INSTANCE::eventToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(
            ConnectableUserDetails userDetails,
            Long artistId,
            ArtistCommentRequest artistCommentRequest) {
        Artist artist = getArtist(artistId);
        User user = getUser(userDetails);

        Comment comment =
                Comment.builder()
                        .user(user)
                        .artist(artist)
                        .contents(artistCommentRequest.getContents())
                        .build();

        commentRepository.save(comment);
    }

    @TimeCheck
    public List<ArtistCommentResponse> getUndeletedArtistComments(Long artistId) {
        List<ArtistComment> undeletedArtistComments =
                commentRepository.getCommentsByArtistId(artistId).stream()
                        .filter(comment -> !comment.isDeleted())
                        .collect(Collectors.toList());

        List<ArtistComment> holderArtistComments =
                findHolderComments(undeletedArtistComments, artistId);
        return holderArtistComments.stream()
                .map(ArtistMapper.INSTANCE::artistCommentToResponse)
                .collect(Collectors.toList());
    }

    private List<ArtistComment> findHolderComments(
            List<ArtistComment> undeletedArtistComments, Long artistId) {
        List<String> artistEventsContactAddresses =
                artistRepository.findArtistEventsContractAddresses(artistId);
        List<String> commentAuthorKlaytnAddresses =
                undeletedArtistComments.stream()
                        .map(ArtistComment::getKlaytnAddress)
                        .distinct()
                        .collect(Collectors.toList());

        TokenIdentifierByKlaytnAddress artistTokenHolders =
                kasService.findTokenHoldingStatus(
                        artistEventsContactAddresses, commentAuthorKlaytnAddresses);
        return parseHolderComments(undeletedArtistComments, artistTokenHolders);
    }

    private List<ArtistComment> parseHolderComments(
            List<ArtistComment> undeletedArtistComments,
            TokenIdentifierByKlaytnAddress artistTokenHolders) {
        List<ArtistComment> holderArtistComments = new ArrayList<>();

        for (ArtistComment artistComment : undeletedArtistComments) {
            String authorKlaytnAddress = artistComment.getKlaytnAddress();
            if (artistTokenHolders.isHolder(authorKlaytnAddress)) {
                TokenIdentifier tokenIdentifier =
                        artistTokenHolders.getTokenIdentifier(authorKlaytnAddress);
                artistComment.addHoldingTicketMetadata(findTicketMetadata(tokenIdentifier));
                holderArtistComments.add(artistComment);
            }
        }

        return holderArtistComments;
    }

    private TicketMetadata findTicketMetadata(TokenIdentifier tokenIdentifier) {
        int tokenId = tokenIdentifier.getTokenId();
        String tokenUri = tokenIdentifier.getTokenUri();
        return ticketRepository.findMetadataByTokenIdAndTokenUri(tokenId, tokenUri);
    }

    @Transactional
    public void deleteComment(ConnectableUserDetails userDetails, Long artistId, Long commentId) {
        Comment comment = getComment(commentId);
        if (!comment.isCommentAuthor(getUser(userDetails))) {
            throw new ConnectableException(HttpStatus.UNAUTHORIZED, ErrorType.NOT_A_COMMENT_AUTHOR);
        }
        commentRepository.deleteComment(artistId, comment.getId());
    }

    private User getUser(ConnectableUserDetails userDetails) {
        return userRepository
                .findByKlaytnAddress(userDetails.getKlaytnAddress())
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.USER_NOT_FOUND));
    }

    private Artist getArtist(Long artistId) {
        return artistRepository
                .findById(artistId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.ARTIST_NOT_EXISTS));
    }

    private Comment getComment(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(
                        () ->
                                new ConnectableException(
                                        HttpStatus.BAD_REQUEST, ErrorType.COMMENT_NOT_EXIST));
    }

    public ArtistNftHolderResponse isArtistNftOwner(
            ConnectableUserDetails userDetails, Long artistId) {
        User user = getUser(userDetails);
        List<String> artistEventContracts =
                eventRepository.findAllEventsByArtistId(artistId).stream()
                        .map(Event::getContractAddress)
                        .collect(Collectors.toList());

        boolean isHolder =
                kasService.checkIsTokenHolder(artistEventContracts, user.getKlaytnAddress());
        if (isHolder) {
            return ArtistNftHolderResponse.isHolder();
        }
        return ArtistNftHolderResponse.isNotHolder();
    }
}
