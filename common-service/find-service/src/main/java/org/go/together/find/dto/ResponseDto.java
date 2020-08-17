package org.go.together.find.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.find.dto.form.PageDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private PageDto page;
    private Collection<T> result;
}
