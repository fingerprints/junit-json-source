package org.fingerprintsoft.junit.json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.params.provider.Arguments;

public class Utils {
    @SuppressWarnings("unchecked")
    public static <R> R map(SimpleDateFormat df, final String value, Type type) {
        if ("null".equals(value)) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.setDateFormat(df);
        try {
            if (value.startsWith("[")) {
                return (R) objectMapper.readValue(value, objectMapper.getTypeFactory().constructCollectionType(List.class, (Class) type));
            }
            if (((Class) type).isAssignableFrom(Date.class)) {
                try {
                    return (R) df.parse(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (((Class) type).isAssignableFrom(String.class)) {
                return (R) value;
            } else {
                try {
                    return (R) objectMapper.readValue(value, (Class) type);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Arguments arguments(SimpleDateFormat df, String jsonString) {
        List<Object> arguments = new ArrayList<>();
        try {
            if (jsonString.startsWith("[")) {
                JSONArray array = new JSONArray(jsonString);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    final String value = object.getString("value");
                    final String type = object.getString("type");
                    Class clazz = Class.forName(type);
                    arguments.add(map(df, value, clazz));
                }

            } else {
                JSONObject object = new JSONObject(jsonString);
                final String value = object.getString("value");
                final String type = object.getString("type");
                Class clazz = Class.forName(type);
                arguments.add(map(df, value, clazz));
            }
        } catch (JSONException | ClassNotFoundException e1) {
            throw new IllegalArgumentException(e1);
        }
        return Arguments.of(arguments.toArray());
    }
}
