package org.go.together.base;

import org.go.together.dto.FormDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @DeleteMapping("/{serviceName}/{id}")
    void delete(@PathVariable("serviceName") String serviceName,
                @PathVariable("id") UUID dtoId);
}
