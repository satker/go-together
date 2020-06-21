package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
public class EventUserDto implements Dto {
    private UUID id;
    private SimpleUserDto user;
    private EventUserStatus userStatus;
    private UUID eventId;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("user name", ComparingObject.builder().getDtoField(() -> this.getUser().getLogin()).isMain(true).build())
                .put("user status", ComparingObject.builder().getDtoField(this::getUserStatus).build())
                .build();
    }
}
