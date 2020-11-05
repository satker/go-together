package org.go.together.find.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.go.together.compare.FieldMapper;
import org.go.together.exceptions.IncorrectFindObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        Pattern pattern = Pattern.compile(REGEX_GROUP);
        Matcher matcher = pattern.matcher(localField);
        if (matcher.find()) {
            return new String[]{matcher.group(0)};
        } else {
            return getParsedFields(getParsedRemoteField(localField)[0]);
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
