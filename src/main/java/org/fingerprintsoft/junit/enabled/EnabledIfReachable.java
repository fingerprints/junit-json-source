package org.fingerprintsoft.junit.enabled;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@ExtendWith(EnabledIfReachableCondition.class)
public @interface EnabledIfReachable {

    String url();

    int timeoutMillis();

}
