package com.backend.connectable.event.mapper;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.dto.EventDetail;
import com.backend.connectable.event.domain.dto.EventTicket;
import com.backend.connectable.event.ui.dto.EventDetailResponse;
import com.backend.connectable.event.ui.dto.EventResponse;
import com.backend.connectable.event.ui.dto.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target="name", source = "eventName")
    @Mapping(target="image", source = "eventImage")
    EventDetailResponse eventDetailToResponse(EventDetail eventDetail);

    @Mapping(target="name", source = "eventName")
    @Mapping(target="image", source = "eventImage")
    @Mapping(target="date", source = "startTime")
    EventResponse eventToResponse(Event event);

    @Mapping(target="ownedBy", ignore = true)
    TicketResponse ticketToResponse(EventTicket eventTicket);
}
