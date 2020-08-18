package org.go.together.find.client;

import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FindClient {
    @PostMapping("/find")
    ResponseDto<Object> find(@RequestBody FormDto formDto);
}
