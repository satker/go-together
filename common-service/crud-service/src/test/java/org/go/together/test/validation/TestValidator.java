package org.go.together.test.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.enums.CrudOperation;
import org.go.together.test.dto.TestDto;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.dto.NumberIntervalDto;
import org.go.together.validation.dto.StringRegexDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestValidator extends CommonValidator<TestDto> {
    private final ManyJoinValidator manyJoinValidator;
    private final JoinTestValidator joinTestValidator;

    public TestValidator(ManyJoinValidator manyJoinValidator,
                         JoinTestValidator joinTestValidator) {
        this.manyJoinValidator = manyJoinValidator;
        this.joinTestValidator = joinTestValidator;
    }

    @Override
    public void getMapsForCheck(TestDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of("test name", TestDto::getName);
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = Map.of(
                "test number", TestDto::getNumber);
        super.SIMPLE_DTO_CORRECT_CHECK = Map.of(
                "simple dto", TestDto::getSimpleDto);
        super.REGEX_STRING_CORRECT_CHECK = Map.of(
                "test name regex", new StringRegexDto(dto.getName(), "^.*test.*$"));
        super.OBJECT_NULL_CHECK = Map.of(
                "test id", TestDto::getId);
        super.DATES_CORRECT_CHECK = Map.of(
                "test dates", new DateIntervalDto(dto.getStartDate(), dto.getEndDate()));
        super.NUMBER_INTERVAL_CORRECT_CHECK = Map.of(
                "test number interval", new NumberIntervalDto(dto.getNumber(), dto.getStartNumber(), dto.getEndNumber()));
        super.COLLECTION_CORRECT_CHECK = Map.of("test elements", TestDto::getElements,
                "test join tests", TestDto::getJoinTestEntities,
                "test many joins", TestDto::getManyJoinEntities);
    }

    @Override
    protected String commonValidation(TestDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        dto.getJoinTestEntities().stream()
                .map((joinTestDto) -> joinTestValidator.validate(joinTestDto, crudOperation))
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        dto.getManyJoinEntities().stream()
                .map(manyJoinDto -> manyJoinValidator.validate(manyJoinDto, crudOperation))
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);
        return errors.toString();
    }

}
