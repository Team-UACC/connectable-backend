package com.backend.connectable.admin.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDeploymentRequest {

    private String contractName;
    private String contractSymbol;
    private String contractAlias;

    private String eventName;
    private String eventDescription;
    private String eventImage;
    private String eventTwitterUrl;
    private String eventInstagramUrl;
    private String eventWebpageUrl;
    private String eventLocation;
    private Long eventArtistId;
    private LocalDateTime eventSalesFrom;
    private LocalDateTime eventSalesTo;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
}
