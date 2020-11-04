package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto extends Dto {
    private String photoUrl;
    private ContentDto content;
}
