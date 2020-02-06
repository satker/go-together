package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventPaidThingDto implements Dto {
    private UUID id;
    private CashCategory cashCategory;
    private PaidThingDto paidThing;
}
