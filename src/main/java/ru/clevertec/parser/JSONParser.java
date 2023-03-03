package ru.clevertec.parser;

import ru.clevertec.exception.JSONParsingException;
import ru.clevertec.format.ClassFormat;
import ru.clevertec.pattern.Patterns;
import ru.clevertec.util.CharacterUtil;
import ru.clevertec.util.ReflectionUtil;
import ru.clevertec.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static ru.clevertec.pattern.Patterns.*;

public class JSONParser implements JSONTemplate {
    @Override
    public <T> T fromJSON(File file, Class<T> obj) {
        try {
            String jsonData = Files.readString(file.toPath());
            return fromJSON(jsonData, obj);
        } catch (IOException | JSONParsingException e) {
            throw new JSONParsingException("Exception parse file: ", e);
        }
    }

    public <T> T fromJSON(String jsonData, Class<T> obj) {
        try {
            if (jsonData == null || jsonData.isEmpty()) return null;
            T instance = obj.getDeclaredConstructor().newInstance();
            ClassFormat formatter = ClassFormat.getInstance();

            Map<String, String> fieldValueMap = getObjectFields(jsonData);
            List<Field> objFields = ReflectionUtil.getAllDeclaredFields(obj);
            objFields.stream()
                    .filter(field -> fieldValueMap.containsKey(field.getName().toLowerCase()))
                    .peek(field -> field.setAccessible(true))
                    .forEach(field -> {
                        try {
                            String value = fieldValueMap.get(field.getName().toLowerCase());
                            Class<?> fieldClass = field.getType();
                            field.set(instance, formatter.checkValueCompatibility(value, fieldClass));
                            field.setAccessible(false);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            // handle reflection-related exceptions
            throw new JSONParsingException("Exception parse json to object: ", e);
        } catch (InvocationTargetException e) {
            // handle InvocationTargetException
            throw new RuntimeException(e);
        }
    }


    @Override
    public void toJSON(File file, Object... objects) {
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder builder = new StringBuilder();
            if (objects.length == 1) {
                builder.append(LEFT_FIGURE_BRACKET);
                builder.append(buildObjectSignature(objects[0]));
                builder.append(RIGHT_FIGURE_BRACKET);

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

    private HashMap<String, String> getObjectFields(String jsonData) {
        HashMap<String, String> fieldValueMap = new HashMap<>();
        jsonData = Utils.cutBrackets(jsonData, LEFT_FIGURE_BRACKET, RIGHT_FIGURE_BRACKET);
        String fieldName;
        String fieldValue;

        for (char c : jsonData.toCharArray()) {
            fieldName = Utils.getName(jsonData);
            jsonData = jsonData.substring(jsonData.indexOf(fieldName) + fieldName.length() + 1).trim();   //to delete field name from json data

            if (CharacterUtil.isString(jsonData.charAt(0))) {
                fieldValue = Utils.getName(jsonData);
            } else if (CharacterUtil.isBoolean(jsonData.charAt(0))) {
                fieldValue = Utils.getBooleanValue(jsonData);
            } else if (CharacterUtil.isNumber(jsonData.charAt(0))) {
                fieldValue = Utils.getNumberValue(jsonData);
            } else if (CharacterUtil.isArray(jsonData.charAt(0))) {
                fieldValue = Utils.getArrayValue(jsonData);
            } else fieldValue = "null";

            jsonData = jsonData.substring(fieldValue.length()).trim();
            fieldValueMap.put(Utils.deleteQuotes(fieldName), Utils.deleteQuotes(fieldValue));

            if (jsonData.length() == 0) {
                break;
            }
        }
        return fieldValueMap;
    }

    private String buildObjectSignature(Object obj) {
        StringBuilder builder = new StringBuilder();
        List<Field> objFields = ReflectionUtil.getAllDeclaredFields(obj.getClass());

        try {
            for (Iterator<Field> iter = objFields.iterator(); iter.hasNext(); ) {
                Field field = iter.next();
                field.setAccessible(true);
                Object value = field.get(obj);
                String fieldValue = "null";
                String fieldName = field.getName().toLowerCase();
                if (Utils.isNull(field)) {
                    throw new NullPointerException("Object " + field.getName() + " is null: ");
                }
                if (Utils.isTextValue(field)) {
                    fieldValue = String.format(FIELD_VALUE_STRING_FORMAT, value);
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new JSONParsingException("Error get fields: ", e);
        }
    }

    private String buildArraySignature(Field field, Object obj) throws IllegalAccessException, NoSuchFieldException {
        Object array = field.get(obj);

        return new StringBuilder().append(arrayIterator(array)).toString();
    }

    private static String arrayIterator(Object obj) {
        StringBuilder builder = new StringBuilder();
        int length = Array.getLength(obj);
        builder.append(LEFT_BRACKET);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(obj, i);
            if (element.getClass().isArray()) {
                builder.append(arrayIterator(element));
            }
            if (Utils.isNumber(element)) {
                builder.append(element);
            } else if (Utils.isTextValue(element)) {
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
