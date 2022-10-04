package com.backend.connectable.admin.ui.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EventIssueRequestTest {

    private Validator validator;

    private EventIssueRequest eventIssueRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        eventIssueRequest = new EventIssueRequest(
            "abc",
            "ABC",
            "abc",
            "eventName",
            "eventDescription",
            "eventImage",
            "eventTwitterUrl",
            "eventInstagramUrl",
            "eventWebpageUrl",
            "eventLocation",
            1L,
            LocalDateTime.of(2000, 1, 1, 0, 0, 0),
            LocalDateTime.of(2000, 1, 1, 1, 0, 0),
            LocalDateTime.of(2000, 1, 1, 2, 0, 0),
            LocalDateTime.of(2000, 1, 1, 3, 0, 0)
        );
    }

    @DisplayName("정상적인 eventIssueRequest에 대해서는 위반 사항이 생기지 않는다")
    @Test
    void valid() {
        // given & when
        Set<ConstraintViolation<EventIssueRequest>> violations = validator.validate(eventIssueRequest);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("contractName에 알파벳, 숫자, 하이픈이 아닌 문자가 들어가면 위반 사항이 발생한다.")
    @Test
    void contractName() {
        // given
        String invalidContractName = "AB12*";
        eventIssueRequest.setContractName(invalidContractName);

        // when
        Set<ConstraintViolation<EventIssueRequest>> violations = validator.validate(eventIssueRequest);

        // then
        assertThat(violations).isNotEmpty();
    }

    @DisplayName("ContractSymbol은 알파벳 대문자 3-4개만 허락한다.")
    @Test
    void contractSymbol() {
        // given
        String invalidContractSymbol = "AB";
        eventIssueRequest.setContractSymbol(invalidContractSymbol);

        // when
        Set<ConstraintViolation<EventIssueRequest>> violations = validator.validate(eventIssueRequest);

        // then
        assertThat(violations).isNotEmpty();
    }

    @DisplayName("ContractAlias는 알파벳 소문자로 시작하며, 알파벳 소문자, 숫자, 하이픈만 허락한다")
    @Test
    void contractAlias() {
        // given
        String invalidContractAlias = "A-alias";
        eventIssueRequest.setContractAlias(invalidContractAlias);

        // when
        Set<ConstraintViolation<EventIssueRequest>> violations = validator.validate(eventIssueRequest);

        // then
        assertThat(violations).isNotEmpty();
    }
}