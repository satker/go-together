package org.go.together.find.correction.path;

import org.go.together.compare.FieldMapper;
import org.go.together.find.correction.path.dto.Path;
import org.go.together.find.dto.Field;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PathCorrectorService implements PathCorrector {
    public Path correct(Field field, Map<String, FieldMapper> fieldMappers) {
        LinkedList<Map<String, FieldMapper>> allFieldMappers = new LinkedList<>();
        allFieldMappers.addLast(fieldMappers);

        String[] paths = field.getPaths();

        String resultPath = getResultPath(allFieldMappers, paths);

        return Path.builder()
                .field(new Field(resultPath))
                .lastFieldMapper(getFieldMapper(allFieldMappers.getLast(), paths)).build();
    }

    private String getResultPath(LinkedList<Map<String, FieldMapper>> allFieldMappers, String[] paths) {
        return Stream.of(paths)
                .map(path -> allFieldMappers.getLast().get(path))
                .peek(fieldMapper -> {
                    if (fieldMapper.getInnerService() != null) {
                        allFieldMappers.addLast(fieldMapper.getInnerService().getMappingFields());
                    }
                })
                .map(FieldMapper::getCurrentServiceField)
                .collect(Collectors.joining("."));
    }

    private FieldMapper getFieldMapper(Map<String, FieldMapper> lastFieldMappers, String[] paths) {
        return lastFieldMappers.get(getLastField(paths));
    }

    private String getLastField(String[] paths) {
        return paths[paths.length - 1];
    }
}
