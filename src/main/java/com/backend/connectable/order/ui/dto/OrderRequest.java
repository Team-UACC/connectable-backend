package com.backend.connectable.order.ui.dto;

import com.backend.connectable.exception.sequence.ValidationGroups;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "입금자 확인을 위해 주문자명은 필수 입력값 입니다.")
    private String userName;

    @NotBlank(message = "입금자 확인을 위해 연락처는 필수 입력값 입니다.",
        groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
        message = "연락처는 xxx-xxxx-xxxx 양식으로 구성되어야 합니다.",
        groups = ValidationGroups.PatternCheckGroup.class)
    private String phoneNumber;

    private List<Long> ticketIds;

    private int amount;

    public OrderRequest(String userName, String phoneNumber, List<Long> ticketIds, int amount) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.ticketIds = ticketIds;
        this.amount = amount;
    }
}
