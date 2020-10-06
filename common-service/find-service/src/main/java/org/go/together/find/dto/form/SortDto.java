package org.go.together.find.dto.form;

import lombok.Data;
import org.go.together.repository.entities.Direction;

@Data
public class SortDto {
    private String field;
    private Direction direction;
}
