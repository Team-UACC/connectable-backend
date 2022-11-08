package com.backend.connectable.artist.ui;

import com.backend.connectable.artist.service.ArtistService;
import com.backend.connectable.artist.ui.dto.ArtistCommentRequest;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<ArtistDetailResponse>> getAllArtists() {
        List<ArtistDetailResponse> artists = artistService.getAllArtists();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{artist-id}")
    public ResponseEntity<ArtistDetailResponse> getArtist(
            @PathVariable("artist-id") Long artistId) {
        ArtistDetailResponse artistDetail = artistService.getArtistDetail(artistId);
        return ResponseEntity.ok(artistDetail);
    }

    @GetMapping("/{artist-id}/events")
    public ResponseEntity<List<EventResponse>> getArtistEvent(
            @PathVariable("artist-id") Long artistId) {
        List<EventResponse> artistEventResponse = artistService.getArtistEvent(artistId);
        return ResponseEntity.ok(artistEventResponse);
    }

    @GetMapping("/{artist-id}/comments")
    public ResponseEntity<List<ArtistCommentResponse>> getArtistComments(
            @PathVariable("artist-id") Long artistId) {
        List<ArtistCommentResponse> artistCommentResponses =
                artistService.getArtistComments(artistId);
        return ResponseEntity.ok(artistCommentResponses);
    }

    @PostMapping("/{artist-id}/comments")
    public ResponseEntity<Void> createArtistComment(
            @AuthenticationPrincipal ConnectableUserDetails userDetails,
            @PathVariable("artist-id") Long artistId,
            @RequestBody ArtistCommentRequest artistCommentRequest) {
        artistService.createComment(userDetails, artistId, artistCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{artist-id}/comments")
    public ResponseEntity<Void> deleteArtistComment(
            @AuthenticationPrincipal ConnectableUserDetails userDetails,
            @PathVariable("artist-id") Long artistId,
            @RequestParam(name = "commentId") Long commentId) {
        artistService.deleteComment(userDetails, artistId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
