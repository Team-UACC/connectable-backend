package com.backend.connectable.artist.mapper;

import com.backend.connectable.artist.domain.dto.ArtistComment;
import com.backend.connectable.artist.domain.dto.ArtistDetail;
import com.backend.connectable.artist.ui.dto.ArtistCommentResponse;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArtistMapper {

    ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    @Mapping(target = "writtenAt", source = "createdDate")
    ArtistCommentResponse artistCommentToResponse(ArtistComment artistComment);

    ArtistDetailResponse artistDetailToResponse(ArtistDetail artistDetail);
}
