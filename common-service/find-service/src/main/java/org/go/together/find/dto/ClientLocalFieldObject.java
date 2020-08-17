package org.go.together.find.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.find.client.FindClient;

@Getter
@Builder
public class ClientLocalFieldObject {
    private final FindClient client;
    private final FieldDto fieldDto;
}
