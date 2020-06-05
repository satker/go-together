package org.go.together.test.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.dto.validation.NumberIntervalDto;
import org.go.together.dto.validation.StringRegexDto;
import org.go.together.logic.Validator;
import org.go.together.test.dto.TestDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
public class TestValidator extends Validator<TestDto> {
    private final ManyJoinValidator manyJoinValidator;
    private final JoinTestValidator joinTestValidator;

    public TestValidator(ManyJoinValidator manyJoinValidator,
                         JoinTestValidator joinTestValidator) {
        this.manyJoinValidator = manyJoinValidator;
        this.joinTestValidator = joinTestValidator;
    }

    @Override
    public void getMapsForCheck(TestDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("test name", dto.getName())
                .build();
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = ImmutableMap.<String, Number>builder()
                .put("test number", dto.getNumber())
                .build();
        super.SIMPLE_DTO_CORRECT_CHECK = ImmutableMap.<String, Collection<SimpleDto>>builder()
                .put("simple dto", Collections.singleton(dto.getSimpleDto()))
                .build();
        super.REGEX_STRING_CORRECT_CHECK = ImmutableMap.<String, StringRegexDto>builder()
                .put("test name regex", new StringRegexDto(dto.getName(), "^.*test.*$"))
                .build();
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("test id", Optional.ofNullable(dto.getId()))
                .build();
        super.DATES_CORRECT_CHECK = ImmutableMap.<String, DateIntervalDto>builder()
                .put("test dates", new DateIntervalDto(dto.getStartDate(), dto.getEndDate()))
                .build();
        super.NUMBER_INTERVAL_CORRECT_CHECK = ImmutableMap.<String, NumberIntervalDto>builder()
                .put("test number interval", new NumberIntervalDto(dto.getNumber(), dto.getStartNumber(), dto.getEndNumber()))
                .build();
        super.COLLECTION_CORRECT_CHECK =
                ImmutableMap.<String, Collection<?>>builder()
                        .put("test elements", dto.getElements())
                        .put("test join tests", dto.getJoinTestEntities())
                        .put("test many joins", dto.getManyJoinEntities())
                        .build();
    }

    protected String commonValidateCustom(TestDto dto) {
        StringBuilder errors = new StringBuilder();

        dto.getJoinTestEntities().stream()
                .map(joinTestValidator::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        dto.getManyJoinEntities().stream()
                .map(manyJoinValidator::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);
        return errors.toString();
    }

}
