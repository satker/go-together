package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class EventPaidThingDto extends ComparableDto {
    private UUID id;
    @ComparingField("cash category")
    private CashCategory cashCategory;
    @ComparingField("paid thing")
    private PaidThingDto paidThing;

    @Override
    public String getMainField() {
        return paidThing.getMainField();
    }
}
