package org.go.together.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.FindService;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.dto.form.PageDto;
import org.go.together.exceptions.ApplicationException;
import org.go.together.find.logic.interfaces.BaseFindService;
import org.go.together.find.logic.interfaces.BaseResultMapper;
import org.go.together.interfaces.Dto;
import org.go.together.mapper.Mapper;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;

public abstract class CommonFindService<D extends Dto, E extends IdentifiedEntity> implements FindService<D> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected Mapper<D, E> mapper;
    protected BaseFindService<E> baseFindService;
    protected CustomRepository<E> repository;
    private BaseResultMapper<D, E> resultMapper;

    @Autowired
    private void setResultMapper(BaseResultMapper<D, E> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Autowired
    public void setMapper(Mapper<D, E> mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRepository(CustomRepository<E> repository) {
        this.repository = repository;
    }

    @Autowired
    private void setMapper(BaseFindService<E> baseFindService) {
        this.baseFindService = baseFindService;
    }

    @SneakyThrows
    public ResponseDto<Object> find(FormDto formDto) {
        final ObjectMapper objectMapper = new ObjectMapper();

        log.info("Started find in '" + getServiceName() + "' with filter: " +
                objectMapper.writeValueAsString(formDto));
        try {
            Pair<PageDto, Collection<Object>> pageDtoResult = baseFindService.find(repository,
                    formDto,
                    getServiceName(),
                    getMappingFields());

            Collection<Object> values = resultMapper.getParsedResult(pageDtoResult, mapper);
            log.info("Find in '" + getServiceName() + "' " + Optional.ofNullable(values)
                    .map(Collection::size)
                    .orElse(0) + " rows with filter: " +
                    objectMapper.writeValueAsString(formDto));
            return new ResponseDto<>(pageDtoResult.getKey(), values);
        } catch (Exception exception) {
            throw new ApplicationException(exception);
        }
    }
}
