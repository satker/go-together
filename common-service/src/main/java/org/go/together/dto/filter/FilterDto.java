package org.go.together.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.logic.find.enums.FindSqlOperator;

import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private FindSqlOperator filterType;
    private Collection<Map<String, Object>> values;

    public void addValue(Collection<Map<String, Object>> values) {
        this.values.addAll(values);
    }
}
