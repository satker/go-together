package org.go.together.find.finders.request;

import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.form.FormDto;

import java.util.Collection;
import java.util.Map;

public interface Sender {
    Map<FieldDto, Collection<Object>> send(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices);
}
