package org.fingerprintsoft.junit.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonFileArgumentsProvider.class)
public @interface JsonFileSource {

    /**
     * The CSV classpath resources to use as the sources of arguments; must not be
     * empty.
     */
    String[] resources();

    /**
     * The encoding to use when reading the CSV files; must be a valid charset.
     *
     * <p>Defaults to {@code "UTF-8"}.
     *
     * @see java.nio.charset.StandardCharsets
     */
    String encoding() default "UTF-8";

}
