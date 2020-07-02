package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
public class EventPaidThingDto implements ComparableDto {
    private UUID id;
    @ComparingField("cash category")
    private CashCategory cashCategory;
    @ComparingField(value = "paid thing", isMain = true)
    private PaidThingDto paidThing;
}
