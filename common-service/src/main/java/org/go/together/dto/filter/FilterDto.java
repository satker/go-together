package org.go.together.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.dto.SimpleDto;
import org.go.together.logic.repository.utils.sql.SqlOperator;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private SqlOperator filterType;
    private Collection<SimpleDto> values;

    public void addValue(Collection<SimpleDto> values) {
        this.values.addAll(values);
    }
}
