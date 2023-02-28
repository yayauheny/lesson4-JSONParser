package ru.clevertec.parser;

import ru.clevertec.exception.JSONParsingException;
import ru.clevertec.pattern.Patterns;
import ru.clevertec.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.util.Arrays;
import java.util.Iterator;

import static ru.clevertec.pattern.Patterns.*;

public class JSONParser implements JSONTemplate {
    public void fromJSON(File file) {

    }

    public void toJSON(File file, Object... objects) {
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder builder = new StringBuilder();
            if (objects.length == 1) {
                builder.append("{");
                builder.append(buildObjectSignature(objects[0]));
                builder.append("}");

                writer.write(builder.toString());
            } else {
                builder.append(LEFT_BRACKET);
                for (Iterator<Object> iter = Arrays.stream(objects).iterator(); iter.hasNext(); ) {
                    Object obj = iter.next();
                    builder.append(LEFT_FIGURE_BRACKET);
                    builder.append(buildObjectSignature(obj));
                    builder.append(RIGHT_FIGURE_BRACKET);

                    if (iter.hasNext()) {
                        builder.append(",");
                    }
                }
                builder.append(RIGHT_BRACKET);
                writer.write(builder.toString());
            }
        } catch (IOException e) {
            throw new JSONParsingException("Error create file: ", e);
        }
    }

    private String buildObjectSignature(Object obj) {
        StringBuilder builder = new StringBuilder();
        Field[] objFields = obj.getClass().getDeclaredFields();

        try {
            for (Iterator<Field> iter = Arrays.stream(objFields).iterator(); iter.hasNext(); ) {
                Field field = iter.next();
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldValue = "null";
                String fieldName = field.getName().toLowerCase();

                if (Utils.isTextValue(field)) {
                    fieldValue = String.format(FIELD_VALUE_STRING_FORMAT, value);
                } else if (Utils.isBoolean(field)) {
                    fieldValue = value.toString();
                } else if (Utils.isArray(field)) {
                    fieldValue = buildArraySignature(field, obj);
                } else if (Utils.isNumber(field)) {
                    fieldValue = value.toString();
                }
                builder.append(String.format(FIELD_VALUE_FORMAT, fieldName, fieldValue));

                if (iter.hasNext()) {
                    builder.append(",");
                }
            }
            return builder.toString();
        } catch (IllegalAccessException e) {
            throw new JSONParsingException("Error get fields: ", e);
        }
    }

    private String buildArraySignature(Field field, Object obj) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        builder.append(LEFT_BRACKET);
        int length = Array.getLength(field.get(obj));

        Object array = field.get(obj);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(array, i);

            if (element instanceof Number) {
                builder.append(element);
            } else if (element instanceof String || element instanceof Byte || element.getClass() == byte.class) {
                builder.append(String.format(FIELD_VALUE_STRING_FORMAT, element));
            }

            if (i != length - 1) {
                builder.append(",");
            }
        }
        builder.append(RIGHT_BRACKET);
        return builder.toString();
    }
}
