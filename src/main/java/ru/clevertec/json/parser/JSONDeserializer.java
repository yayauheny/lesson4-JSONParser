package ru.clevertec.json.parser;

import ru.clevertec.json.exception.JSONParsingException;
import ru.clevertec.json.format.ClassFormat;
import ru.clevertec.json.pattern.Patterns;
import ru.clevertec.json.util.CharacterUtil;
import ru.clevertec.json.util.ReflectionUtil;
import ru.clevertec.json.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JSONDeserializer {
    private static JSONDeserializer INSTANCE;
    private static CharacterUtil characterUtil = CharacterUtil.getInstance();
    private static ReflectionUtil reflectionUtil = ReflectionUtil.getInstance();
    private static StringUtil stringUtil = StringUtil.getInstance();

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
            List<Field> objFields = reflectionUtil.getAllDeclaredFields(obj);
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
                            throw new JSONParsingException("Exception parse json to object: ", e);
                        }
                    });
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new JSONParsingException("Exception parse json to object: ", e);
        }
    }

    private HashMap<String, String> getObjectFields(String jsonData) {
        HashMap<String, String> fieldValueMap = new HashMap<>();
        jsonData = stringUtil.cutBrackets(jsonData, Patterns.LEFT_FIGURE_BRACKET, Patterns.RIGHT_FIGURE_BRACKET);
        String fieldName;
        String fieldValue;

        for (char c : jsonData.toCharArray()) {
            fieldName = stringUtil.getName(jsonData);
            jsonData = jsonData.substring(jsonData.indexOf(fieldName) + fieldName.length() + 1).trim();   //to delete field name from json data

            if (characterUtil.isString(jsonData.charAt(0))) {
                fieldValue = stringUtil.getName(jsonData);
            } else if (characterUtil.isBoolean(jsonData.charAt(0))) {
                fieldValue = stringUtil.getBooleanValue(jsonData);
            } else if (characterUtil.isNumber(jsonData.charAt(0))) {
                fieldValue = stringUtil.getNumberValue(jsonData);
            } else if (characterUtil.isArray(jsonData.charAt(0))) {
                fieldValue = stringUtil.getArrayValue(jsonData);
            } else fieldValue = "null";

            jsonData = jsonData.substring(fieldValue.length()).trim();
            fieldValueMap.put(stringUtil.deleteQuotes(fieldName), stringUtil.deleteQuotes(fieldValue));

            if (jsonData.length() == 0) {
                break;
            }
        }
        return fieldValueMap;
    }

    private JSONDeserializer() {
    }

    public static JSONDeserializer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JSONDeserializer();
        }
        return INSTANCE;
    }
}
