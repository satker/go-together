package org.go.together.dto;

import lombok.Data;
import org.go.together.enums.Direction;

@Data
public class SortDto {
    private String field;
    private Direction direction;
}
