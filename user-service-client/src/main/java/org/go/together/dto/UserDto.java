package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto implements Dto {
    private UUID id;
    private String login;
    private String mail;
    private String firstName;
    private String lastName;
    private LocationDto location;
    private String description;
    private String password;
    private Role role;
    private Set<PhotoDto> userPhotos;
    private Set<LanguageDto> languages;
    private Set<InterestDto> interests;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("login", ComparingObject.builder().getDtoField(this::getLogin).isMain(true).build())
                .put("mail", ComparingObject.builder().getDtoField(this::getMail).build())
                .put("first name", ComparingObject.builder().getDtoField(this::getFirstName).build())
                .put("last name", ComparingObject.builder().getDtoField(this::getLastName).build())
                .put("location", ComparingObject.builder().getDtoField(this::getLocation).build())
                .put("description", ComparingObject.builder().getDtoField(this::getDescription).build())
                .put("password", ComparingObject.builder().getDtoField(this::getPassword).isNeededDeepCompare(false).build())
                .put("role", ComparingObject.builder().getDtoField(this::getRole).build())
                .put("user photos", ComparingObject.builder().getDtoField(this::getUserPhotos).build())
                .put("languages", ComparingObject.builder().getDtoField(this::getLanguages).build())
                .put("interests", ComparingObject.builder().getDtoField(this::getInterests).build())
                .build();
    }

    @Override
    public UUID getAuthorId() {
        return getId();
    }
}
