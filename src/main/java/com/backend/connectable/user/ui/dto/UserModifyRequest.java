package com.backend.connectable.user.ui.dto;

import com.backend.connectable.exception.sequence.ValidationGroups.NotEmptyGroup;
import com.backend.connectable.exception.sequence.ValidationGroups.PatternCheckGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

    @NotBlank(message = "닉네임은 필수 입력값 입니다.",
        groups = NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,20}$",
        message = "닉네임은 2자 이상 20자 이하, 영어 또는 숫자 또는 한글로 구성된 닉네임이여야 합니다.",
        groups = PatternCheckGroup.class)
    private String nickname;

    @NotBlank(message = "연락처는 필수 입력값 입니다.",
        groups = NotEmptyGroup.class)
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
        message = "연락처는 xxx-xxxx-xxxx 양식으로 구성되어야 합니다.",
        groups = PatternCheckGroup.class)
    private String phoneNumber;
}
