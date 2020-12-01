package org.go.together.base;

import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FindClient {
    @PostMapping("/find")
    ResponseDto<Object> find(@RequestBody FormDto formDto);
}
