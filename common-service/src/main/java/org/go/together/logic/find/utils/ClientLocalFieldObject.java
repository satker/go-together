package org.go.together.logic.find.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.go.together.interfaces.FindClient;

@Getter
@Setter
@Builder
public class ClientLocalFieldObject {
    private FindClient client;
    private String localField;
}
