package org.go.together.find.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.go.together.find.utils.FindUtils.*;

@Getter
@EqualsAndHashCode
public class Field {
    private final String localField;
    private final String remoteField;

    public Field(String searchField) {
        this.localField = getParsedRemoteField(searchField)[0];
        this.remoteField = getAnotherServiceFilter(searchField);
    }

    public Field(String localField, String remoteField) {
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

    @Override
    public String toString() {
        return localField;
    }
}
