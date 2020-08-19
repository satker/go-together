package org.go.together.find.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getFilterFields(){
        String[] paths = this.getPaths();
        return paths[paths.length - 1];
    }

    @Override
    public String toString() {
        return localField;
    }
}
