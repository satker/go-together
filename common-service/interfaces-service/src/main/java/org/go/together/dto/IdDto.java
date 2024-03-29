package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Identified;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDto implements Identified {
    private UUID id;
}
