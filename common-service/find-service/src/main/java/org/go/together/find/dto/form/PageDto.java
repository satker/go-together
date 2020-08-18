package org.go.together.find.dto.form;

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
