package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class PaidThingDto extends ComparableDto {
    private UUID id;

    @ComparingField("paid thing name")
    private String name;

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getMainField() {
        return name;
    }
}
