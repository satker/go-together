package org.go.together.notification.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventPaidThingDto implements Dto {
    private UUID id;
    @ComparingField("cash category")
    private CashCategory cashCategory;
    @ComparingField(value = "paid thing", isMain = true)
    private PaidThingDto paidThing;
}