package org.go.together.kafka.consumer.impl;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.Dto;
import org.go.together.kafka.consumers.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonCrudKafkaConsumer<D extends Dto> implements KafkaConsumer<D> {
    protected CrudService<D> service;
    protected Validator<D> validator;
    protected FindService<D> findService;

    @Autowired
    public void setFindService(FindService<D> findService) {
        this.findService = findService;
    }

    @Autowired
    public void setValidator(Validator<D> validator) {
        this.validator = validator;
    }

    @Autowired
    public void setService(CrudService<D> service) {
        this.service = service;
    }
}
