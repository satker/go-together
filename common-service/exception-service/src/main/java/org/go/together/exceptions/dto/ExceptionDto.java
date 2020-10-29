package org.go.together.exceptions.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionDto {
    private final String exceptionMessage;
}
