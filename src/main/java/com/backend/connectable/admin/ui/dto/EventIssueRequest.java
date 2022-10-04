package com.backend.connectable.admin.ui.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventIssueRequest {

    @Pattern(regexp = "^[A-Za-z\\d\\-]+$", message = "ContractName은 알파벳/숫자/하이픈만 허락합니다.")
    private String contractName;

    @Pattern(regexp = "[A-Z]{3,4}", message = "ContractSymbol은 알파벳 대문자 3-4개로 구성되어야 합니다.")
    private String contractSymbol;

    @Pattern(regexp = "^[a-z][a-z\\d\\-]+$", message = "ContractAlias는 알파벳 소문자, 숫자, 하이픈으로 구성됩니다. 맨 앞 글자는 알파벳 소문자입니다.")
    private String contractAlias;

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventDescription;

    @NotBlank
    private String eventImage;

    private String eventTwitterUrl;
    private String eventInstagramUrl;
    private String eventWebpageUrl;

    @NotBlank
    private String eventLocation;

    @NotNull
    private Long eventArtistId;

    @NotNull
    private LocalDateTime eventSalesFrom;

    @NotNull
    private LocalDateTime eventSalesTo;

    @NotNull
    private LocalDateTime eventStartTime;

    @NotNull
    private LocalDateTime eventEndTime;
}
