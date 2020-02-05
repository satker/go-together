package org.go.together.validation;

import org.assertj.core.util.Strings;
import org.go.together.dto.PhotoDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PhotoValidator extends Validator<PhotoDto> {
    private static final String REGEX_CHECK_URL =
            "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    public void getMapsForCheck(PhotoDto photoDto) {
    }

    @Override
    protected String commonValidateCustom(PhotoDto photo) {
        StringBuilder errors = new StringBuilder();
        boolean contentIsNull = photo.getContent() == null || (Strings.isNullOrEmpty(photo.getContent().getType()) &&
                (photo.getContent().getPhotoContent() == null ||
                        photo.getContent().getPhotoContent().length == 0));
        if (Strings.isNullOrEmpty(photo.getPhotoUrl()) && contentIsNull) {
            errors.append("Фото должно содержать файл или ссылку.");
        } else if (Strings.isNullOrEmpty(photo.getPhotoUrl())) {
            if (Strings.isNullOrEmpty(photo.getContent().getType())) {
                errors.append("Передан неккоректный файл");
            } else if (!Pattern.compile("^data:image/(png|jpg|jpeg);base64,")
                    .matcher(photo.getContent().getType()).matches()) {
                errors.append("Неверно указан тип файла");
            }
        } else if (!contentIsNull) {
            if (!Pattern.compile(REGEX_CHECK_URL).matcher(photo.getPhotoUrl()).matches()) {
                errors.append("Передана неккоректная ссылка");
            }
        }
        return errors.toString();
    }
}
