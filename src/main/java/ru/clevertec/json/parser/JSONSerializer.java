package ru.clevertec.json.parser;

import ru.clevertec.json.exception.JSONParsingException;
import ru.clevertec.json.pattern.Patterns;
import ru.clevertec.json.util.ReflectionUtil;
import ru.clevertec.json.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONSerializer {
    private static JSONSerializer INSTANCE;
    private static ReflectionUtil reflectionUtil = ReflectionUtil.getInstance();
    private static Utils util = Utils.getInstance();


    public void toJSON(File file, Object... objects) {
        try (FileWriter writer = new FileWriter(file)) {
            String jsonData = toJSON(objects);
            writer.write(jsonData);
        } catch (IOException e) {
            throw new JSONParsingException("Exception create file: ", e);
        }
    }

    public String toJSON(Object... objects) {
        StringBuilder builder = new StringBuilder();
        if (objects.length == 1) {
            builder.append(Patterns.LEFT_FIGURE_BRACKET);
            builder.append(buildObjectSignature(objects[0]));
            builder.append(Patterns.RIGHT_FIGURE_BRACKET);

            return builder.toString();
        } else {
            builder.append(Patterns.LEFT_BRACKET);
            for (Iterator<Object> iter = Arrays.stream(objects).iterator(); iter.hasNext(); ) {
                Object obj = iter.next();
                builder.append(Patterns.LEFT_FIGURE_BRACKET);
                builder.append(buildObjectSignature(obj));
                builder.append(Patterns.RIGHT_FIGURE_BRACKET);

                if (iter.hasNext()) {
                    builder.append(",");
                }
            }
            builder.append(Patterns.RIGHT_BRACKET);
            return builder.toString();
        }
    }

    private String buildObjectSignature(Object obj) {
        StringBuilder builder = new StringBuilder();
        List<Field> objFields = reflectionUtil.getAllDeclaredFields(obj.getClass());

        try {
            for (Iterator<Field> iter = objFields.iterator(); iter.hasNext(); ) {
                Field field = iter.next();
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldValue = null;
                String fieldName = field.getName().toLowerCase();

                if (reflectionUtil.isTextValue(field) && !util.isNull(value)) {
                    fieldValue = String.format(Patterns.FIELD_VALUE_STRING_FORMAT, value);
                } else if (reflectionUtil.isArray(field)) {
                    fieldValue = buildArraySignature(field, obj);
                } else if (reflectionUtil.isNumber(field)) {
                    fieldValue = value.toString();
                }
                builder.append(String.format(Patterns.FIELD_VALUE_FORMAT, fieldName, fieldValue));

                if (iter.hasNext()) {
                    builder.append(",");
                }
            }
            return builder.toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new JSONParsingException("Exception get fields: ", e);
        }
    }

    private String buildArraySignature(Field field, Object obj) throws IllegalAccessException, NoSuchFieldException {
        Object array = field.get(obj);

        return new StringBuilder().append(arrayIterator(array)).toString();
    }

    private static String arrayIterator(Object obj) {
        if(obj == null) return "null";
        StringBuilder builder = new StringBuilder();
        int length = Array.getLength(obj);
        builder.append(Patterns.LEFT_BRACKET);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(obj, i);
            if (element.getClass().isArray()) {
                builder.append(arrayIterator(element));
            }
            if (util.isNumber(element)) {
                builder.append(element);
            } else if (util.isTextValue(element)) {
                builder.append(String.format(Patterns.FIELD_VALUE_STRING_FORMAT, element));
            }
            if (i != length - 1) {
                builder.append(",");
            }
        }
        builder.append(Patterns.RIGHT_BRACKET);
        return builder.toString();
    }

    private JSONSerializer() {
    }

    public static JSONSerializer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JSONSerializer();
        }
        return INSTANCE;
    }
}
