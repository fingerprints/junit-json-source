package org.fingerprintsoft.junit.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

public class JsonArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<JsonSource> {

    private String[] value;
    private final SimpleDateFormat df;

    public JsonArgumentsProvider() {
        df = new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void accept(final JsonSource annotation) {
        value = annotation.value();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        return Arrays.stream(value).map(
                jsonString -> Utils.arguments(df, jsonString)
        );
    }

}
