package org.go.together.kafka.consumer.impl;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.Dto;
import org.go.together.kafka.consumers.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonCrudKafkaConsumer<D extends Dto> implements KafkaConsumer<D> {
}
