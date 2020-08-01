package org.go.together.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComparingField {
    String value();

    boolean deepCompare() default true;

    boolean isMain() default false;

    boolean idCompare() default false;
}
