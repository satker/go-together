package org.go.together.kafka.impl.producers;

import org.go.together.kafka.interfaces.producers.crud.DeleteKafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;


public abstract class CommonDeleteKafkaProducer implements DeleteKafkaProducer {
    private KafkaTemplate<UUID, UUID> kafkaTemplate;

    public KafkaTemplate<UUID, UUID> getKafkaTemplate() {
        return kafkaTemplate;
    }

    //@Autowired
    public void setDeleteKafkaTemplate(KafkaTemplate<UUID, UUID> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
