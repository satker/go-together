package org.go.together.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAutoConfigurationKafkaProducer {
    String producerId();

    boolean isCreate() default true;

    boolean isUpdate() default true;

    boolean isValidate() default true;

    boolean isRead() default true;

    boolean isFind() default true;

    boolean isDelete() default true;
}
