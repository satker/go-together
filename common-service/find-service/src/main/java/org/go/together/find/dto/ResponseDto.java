package org.go.together.find.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private PageDto page;
    private Collection<T> result;
}
