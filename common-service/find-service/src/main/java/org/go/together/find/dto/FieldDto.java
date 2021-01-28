package org.go.together.find.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.FieldMapper;
import org.go.together.exceptions.IncorrectFindObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.*;

@Getter
@EqualsAndHashCode
public class FieldDto {
    private final String localField;
    private final String remoteField;

    public FieldDto(String searchField) {
        this.localField = getParsedRemoteField(searchField)[0];
        this.remoteField = getAnotherServiceFilter(searchField);
    }

    public FieldDto(String localField, String remoteField) {
        this.localField = localField;
        this.remoteField = remoteField;
    }

    private String getAnotherServiceFilter(String string) {
        String[] otherServiceFields = getParsedRemoteField(string);
        if (otherServiceFields.length > 1) {
            return otherServiceFields[1];
        }
        return null;
    }

    public String[] getPaths() {
        return getParsedFields(getParsedRemoteField(localField)[0]);
    }

    public List<String> getFieldsAndOperators() {
        String[] splitField = localField.split("\\[");
        if (StringUtils.isNotBlank(remoteField)) {
            return Collections.singletonList(localField + "?" + remoteField);
        }
        if (splitField.length == 1) {
            return Collections.singletonList(splitField[0]);
        } else {
            String prefix = splitField[0];
            List<String> fields = new LinkedList<>();

            String[] group = splitField[1].replaceAll("]", StringUtils.EMPTY)
                    .split("\\|" + GROUP_OR + GROUP_AND);
            for (int i = 0; i < group.length; i++) {
                fields.add(prefix + group[i]);
                if (i < group.length - 1) {
                    fields.add(getDelimiter(localField, group[i]));
                }
            }
            return fields;
        }
    }

    public String getFilterFields() {
        String[] paths = this.getPaths();
        return paths[paths.length - 1];
    }

    public Map<String, FieldMapper> getFieldMappersByFieldDto(Map<String, FieldMapper> availableFields) {
        String[] localEntityFullFields = getPaths();

        String localEntityField = localEntityFullFields[0];
        String[] singleGroupFields = getSingleGroupFields(localEntityField);
        try {
            return Stream.of(singleGroupFields)
                    .collect(Collectors.toMap(this::getFirstField,
                            field -> getFieldMapper(availableFields, field),
                            (fieldMapper, fieldMapper2) -> fieldMapper));
        } catch (Exception exception) {
            throw new IncorrectFindObject("Field " + toString() + " is unavailable for search.");
        }
    }

    private FieldMapper getFieldMapper(Map<String, FieldMapper> availableFields,
                                       String field) {
        String firstField = getFirstField(field);
        return availableFields.get(firstField);
    }

    private String getFirstField(String field) {
        if (field.contains(".")) {
            String[] splitByCommaString = getParsedFields(field);
            return splitByCommaString[0];
        }
        return field;
    }

    @Override
    public String toString() {
        return localField;
    }
}
