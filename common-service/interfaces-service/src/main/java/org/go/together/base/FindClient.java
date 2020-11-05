package org.go.together.base;

import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.dto.form.FormDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface FindClient {
    @PostMapping("/find")
    ResponseDto<Object> find(@RequestBody FormDto formDto);

    @PostMapping("/validate/{serviceName}")
    ValidationMessageDto validate(@PathVariable("serviceName") String serviceName,
                                  @RequestBody Object dto);

    @PutMapping("/{serviceName}")
    IdDto create(@PathVariable("serviceName") String serviceName,
                 @RequestBody Object dto);

    @PostMapping("/{serviceName}")
    IdDto update(@PathVariable("serviceName") String serviceName,
                 @RequestBody Object dto);
}
