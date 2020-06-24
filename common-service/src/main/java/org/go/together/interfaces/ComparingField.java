package org.go.together.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComparingField {
    String value();

    boolean deepCompare() default true;

    boolean isMain() default false;
}
