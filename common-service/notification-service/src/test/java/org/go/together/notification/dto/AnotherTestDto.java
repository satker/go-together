package org.go.together.notification.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder(toBuilder = true)
public class AnotherTestDto extends ComparableDto {
    private UUID id;

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
}
