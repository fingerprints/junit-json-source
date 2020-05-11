package org.fingerprintsoft.junit.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.PreconditionViolationException;
import org.junit.platform.commons.util.Preconditions;

import static java.util.stream.StreamSupport.stream;

public class JsonFileArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<JsonFileSource> {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider;

    private JsonFileSource annotation;
    private String[] resources;
    private Charset charset;
    private final SimpleDateFormat df;

    JsonFileArgumentsProvider() {
        this(Class::getResourceAsStream);
    }

    JsonFileArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
        df = new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void accept(JsonFileSource annotation) {
        this.annotation = annotation;
        resources = annotation.resources();
        try {
            this.charset = Charset.forName(annotation.encoding());
        } catch (Exception ex) {
            throw new PreconditionViolationException("The charset supplied in " + this.annotation + " is invalid", ex);
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Arrays.stream(resources)
                .map(resource -> openInputStream(context, resource))
                .flatMap(inputStream -> {
                    try {
                        return toArguments(inputStream);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private InputStream openInputStream(ExtensionContext context, String resource) {
        Preconditions.notBlank(resource, "Classpath resource [" + resource + "] must not be null or blank");
        Class<?> testClass = context.getRequiredTestClass();
        return Preconditions.notNull(inputStreamProvider.apply(testClass, resource),
                () -> "Classpath resource [" + resource + "] does not exist");
    }

    private Stream<Arguments> toArguments(InputStream inputStream) throws Exception {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        String jsonString = textBuilder.toString();
        JSONArray array = new JSONArray(jsonString);

        return stream(array.spliterator(), false).map(
                o -> Utils.arguments(df, o.toString())
        );
    }

}
