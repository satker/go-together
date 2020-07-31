package org.go.together.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.go.together.client.FindClient;

@Getter
@Setter
@Builder
public class ClientLocalFieldObject {
    private FindClient client;
    private String localField;
}
