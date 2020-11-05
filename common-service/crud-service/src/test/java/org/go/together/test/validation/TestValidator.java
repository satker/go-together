package org.go.together.test.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.enums.CrudOperation;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.test.dto.ManyJoinDto;
import org.go.together.test.dto.TestDto;
import org.go.together.validation.Validator;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.dto.NumberIntervalDto;
import org.go.together.validation.dto.StringRegexDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class TestValidator extends CommonValidator<TestDto> {
    private final Validator<ManyJoinDto> manyJoinValidator;
    private final Validator<JoinTestDto> joinTestValidator;

    public TestValidator(Validator<ManyJoinDto> manyJoinValidator,
                         Validator<JoinTestDto> joinTestValidator) {
        this.manyJoinValidator = manyJoinValidator;
        this.joinTestValidator = joinTestValidator;
    }

    @Override
    public Map<String, Function<TestDto, ?>> getMapsForCheck() {
        return Map.of("test name", TestDto::getName,
                "test number", TestDto::getNumber,
                "simple dto", TestDto::getSimpleDto,
                "test name regex", testDto -> new StringRegexDto(testDto.getName(), "^.*test.*$"),
                "test id", TestDto::getId,
                "test dates", testDto -> new DateIntervalDto(testDto.getStartDate(), testDto.getEndDate()),
                "test number interval", testDto -> new NumberIntervalDto(testDto.getNumber(),
                        testDto.getStartNumber(), testDto.getEndNumber()),
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
