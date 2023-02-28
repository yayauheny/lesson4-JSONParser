package ru.clevertec.util;

import ru.clevertec.exception.JSONParsingException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public void getObject(String fieldName) {

    }

    public static boolean isTextValue(Field field) {
        return field.getType() == String.class || field.getType() == Character.class
                || field.getType() == char.class;
    }

    public static boolean isBoolean(Field field) {
        return field.getType() == Boolean.class || field.getType() == boolean.class;
    }


    public static boolean isNumber(Field field) {
        return Number.class.isAssignableFrom(field.getType())
                || field.getType().isPrimitive();
    }

    public static boolean isArray(Field field) {
        return field.getType().isArray() ||
                List.class.isAssignableFrom(field.getType());
    }

    public static boolean isList(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    public static boolean isMap(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }
}
