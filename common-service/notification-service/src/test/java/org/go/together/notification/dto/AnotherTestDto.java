package org.go.together.notification.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnotherTestDto extends ComparableDto {
    @ComparingField("another string")
    private String string;

    @ComparingField("another number")
    private Number number;

    @Override
    public UUID getOwnerId() {
        return null;
    }

    @Override
    public UUID getParentId() {
        return null;
    }

    @Override
    public String getMainField() {
        return string;
    }
}
