package org.go.together.find.finders.request;

import org.go.together.dto.form.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SenderService implements Sender {
    public Map<FieldDto, Collection<Object>> send(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices) {
        return filtersToAnotherServices.entrySet().parallelStream()
                .map(this::getRemoteResult)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        this::resolveRequestResult));
    }

    private Collection<Object> resolveRequestResult(Collection<Object> result1, Collection<Object> result2) {
        result1.addAll(result2);
        return result1;
    }

    private Map.Entry<FieldDto, Collection<Object>> getRemoteResult(Map.Entry<ClientLocalFieldObject, FormDto> entry) {
        try {
            Collection<Object> result = entry.getKey().getClient().find(entry.getValue()).getResult();
            return Map.entry(entry.getKey().getFieldDto(), result);
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
