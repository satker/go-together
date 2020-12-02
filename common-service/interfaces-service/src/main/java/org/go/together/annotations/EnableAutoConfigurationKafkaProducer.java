package org.go.together.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface EnableAutoConfigurationKafkaProducer {
    String producerId();
}
