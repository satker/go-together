package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.Dto;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<D extends Dto> {
    private PageDto page;
    private Collection<D> result;
}
