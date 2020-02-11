package org.go.together.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private Integer page;
    private Integer size;
    private Long totalSize;
    private Collection<SortDto> sort;
}
