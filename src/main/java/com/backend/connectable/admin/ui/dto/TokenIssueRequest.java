package com.backend.connectable.admin.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenIssueRequest {

    @NotBlank private String contractAddress;

    @NotNull private Integer startTokenId;

    @NotNull private Integer endTokenId;

    @NotBlank private String tokenUri;

    @NotNull private Integer price;
}
