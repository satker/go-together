package org.go.together.base;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.interfaces.Dto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FindClient {
    @PostMapping("/find")
    ResponseDto<Object> find(@RequestBody FormDto formDto);

    @PostMapping("/validate")
    <T extends Dto> String validate(@RequestBody T routeInfo);
}
