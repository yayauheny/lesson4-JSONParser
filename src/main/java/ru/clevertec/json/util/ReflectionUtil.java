package ru.clevertec.json.util;

import ru.clevertec.json.format.ClassFormat;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static ru.clevertec.json.pattern.Patterns.LEFT_BRACKET;
import static ru.clevertec.json.pattern.Patterns.RIGHT_BRACKET;

public final class ReflectionUtil {
    private static ReflectionUtil INSTANCE;
    public boolean isNull(Field field){
        return field == null;
    }

    public List<Field> getAllDeclaredFields(Class<?> aClass) {
        if (aClass == null) return Collections.emptyList();
        final Class<?> superclass = aClass.getSuperclass();
        return Stream.concat(Arrays.stream(aClass.getDeclaredFields()), getAllDeclaredFields(superclass).stream())
                .toList();
    }

    public <T> T parseArray(String s, Class<T> type) {
        String[] jsonData = s.split(",");

        for (int i = 0; i < jsonData.length; i++) {  //for deleting all spaces from values
            if (jsonData[i].contains(LEFT_BRACKET)) {
                jsonData[i] = jsonData[i].replace('[', ' ');
            } else if (jsonData[i].contains(RIGHT_BRACKET)) {
                jsonData[i] = jsonData[i].replace(']', ' ');
            }
            jsonData[i] = jsonData[i].trim();
        }

        ClassFormat formatter = ClassFormat.getInstance();
        Object result = Array.newInstance(type.getComponentType(), jsonData.length);

        if (type.isArray()) {
            type = (Class<T>) type.getComponentType();
        }

        for (int i = 0; i < jsonData.length; i++) {
            String trimmed = jsonData[i].trim();
            Array.set(result, i, formatter.checkValueCompatibility(trimmed, type));
        }
        return (T) result;
    }

    public boolean isTextValue(Field field) {
        return field.getType() == String.class || field.getType() == Character.class
                || field.getType() == char.class;
    }

    public boolean isNumber(Field field) {
        return Number.class.isAssignableFrom(field.getType())
                || field.getType().isPrimitive();
    }

    public boolean isArray(Field field) {
        return field.getType().isArray() ||
                List.class.isAssignableFrom(field.getType());
    }

    private ReflectionUtil() {
    }

    public static ReflectionUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReflectionUtil();
        }
        return INSTANCE;
    }
}
