package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventPaidThingDto implements Dto {
    private UUID id;
    @ComparingField(value = "cash category", isMain = true)
    private CashCategory cashCategory;
    @ComparingField("paid thing")
    private PaidThingDto paidThing;
}
