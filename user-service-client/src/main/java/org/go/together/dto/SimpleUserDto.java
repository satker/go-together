package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
public class SimpleUserDto implements Dto {
    private UUID id;
    private String login;
    private String firstName;
    private String lastName;
    private PhotoDto userPhoto;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("login", ComparingObject.builder().getDtoField(this::getLogin).isMain(true).build())
                .put("first name", ComparingObject.builder().getDtoField(this::getFirstName).build())
                .put("last name", ComparingObject.builder().getDtoField(this::getLastName).build())
                .put("user photo", ComparingObject.builder().getDtoField(this::getUserPhoto).build())
                .build();
    }
}
