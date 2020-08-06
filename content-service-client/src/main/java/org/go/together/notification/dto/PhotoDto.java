package org.go.together.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto implements Dto {
    private UUID id;

    @ComparingField("photo url")
    private String photoUrl;

    @ComparingField(value = "content", deepCompare = false)
    private ContentDto content;
}
