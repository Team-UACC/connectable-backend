package com.backend.connectable.admin.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
