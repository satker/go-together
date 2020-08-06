package org.go.together.find.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.go.together.find.client.FindClient;

@Getter
@Setter
@Builder
public class ClientLocalFieldObject {
    private FindClient client;
    private String localField;
}
