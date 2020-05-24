package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.dto.filter.PageDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private PageDto page;
    private Collection result;
}
