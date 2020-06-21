package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
public class EventPaidThingDto implements Dto {
    private UUID id;
    private CashCategory cashCategory;
    private PaidThingDto paidThing;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("cash category", ComparingObject.builder().getDtoField(this::getCashCategory).isMain(true).build())
                .put("paid thing", ComparingObject.builder().getDtoField(this::getPaidThing).build())
                .build();
    }
}
