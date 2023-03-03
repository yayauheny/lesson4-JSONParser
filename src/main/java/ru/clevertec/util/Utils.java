package ru.clevertec.util;

import ru.clevertec.exception.JSONParsingException;
import ru.clevertec.pattern.Patterns;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static ru.clevertec.pattern.Patterns.LEFT_FIGURE_BRACKET;
import static ru.clevertec.pattern.Patterns.RIGHT_FIGURE_BRACKET;

public class Utils {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNull(String str) {
        return "null".equalsIgnoreCase(str);
    }

    public static boolean isFieldName(String str) {
        return str.startsWith("\"") && str.endsWith("\"");
    }

    public static boolean isArrayValue(String str) {
        return str.startsWith(Patterns.LEFT_BRACKET) && str.endsWith(Patterns.RIGHT_BRACKET);
    }


    public static boolean isTextValue(Field field) {
        return field.getType() == String.class || field.getType() == Character.class
                || field.getType() == char.class;
    }

    public static boolean isTextValue(Object obj) {
        return obj instanceof String || obj instanceof Character
                || obj.getClass() == char.class;
    }

    public static boolean isBoolean(Field field) {
        return field.getType() == Boolean.class || field.getType() == boolean.class;
    }

    public static boolean isBoolean(Object obj) {
        return obj instanceof Boolean || obj.getClass() == boolean.class;
    }


    public static boolean isNumber(Field field) {
        return Number.class.isAssignableFrom(field.getType())
                || field.getType().isPrimitive();
    }

    public static boolean isNumber(Object obj) {
        return obj instanceof Number || obj.getClass().isPrimitive() &&
                obj.getClass() != char.class && obj.getClass() != boolean.class;
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

    public static String getObject(String jsonData) {
        StringBuilder builder = new StringBuilder();
        int brackets = 0;
        for (char dataSymbol : jsonData.toCharArray()) {
            if (dataSymbol == '{') brackets++;
            if (dataSymbol == '}') brackets--;
            builder.append(dataSymbol);
            if (brackets == 0) break;
        }
        return builder.toString();
    }

    public static String getName(String jsonData) {
        StringBuilder builder = new StringBuilder();
        int quotes = 0;
        for (char dataSymbol : jsonData.toCharArray()) {
            if (quotes == 2) break;
            if (dataSymbol == '"') quotes++;
            if (quotes > 0) builder.append(dataSymbol);
        }
        return builder.toString();
    }

    public static String[] getObjectFields(String str) {
        String[] fields = str.split(",");
        Arrays.stream(fields).forEach(field -> field.trim());

        return fields;
    }

    public static void putAllFields(String[] array, Map<String, String> map) {
        Arrays.stream(array)
                .map(field -> field.split(Patterns.DELIMITER))
                .forEach(fieldNameValue -> {
                    map.put(deleteQuotes(fieldNameValue[0].trim()),
                            deleteQuotes(deleteComma(fieldNameValue[1])));

                });
    }


    public static String deleteQuotes(String value) {
        int quotes = 0;
        for (char c : value.toCharArray()) {
            if (c == '\"') quotes++;
            if (c != '\"') break;
        }
        value = value.trim().substring(quotes, value.length() - quotes);
        return value;
    }

    public static String deleteComma(String value) {
        return value.endsWith(",") ? value.substring(value.length() - 1) : value;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isString(char c) {
        return c == '"';
    }

    private static boolean isArray(char c) {
        return c == '[';
    }

    private static boolean isObject(char c) {
        return c == '{';
    }

    public static boolean isObject(String s) {
        return (s.startsWith(LEFT_FIGURE_BRACKET) && s.endsWith(RIGHT_FIGURE_BRACKET));
    }

    public static boolean isBoolean(String s) {
        return "false".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s);
    }

    public static boolean isNumber(String s) {
        return Pattern.compile(Patterns.NUMBER_FORMAT).matcher(s).find();
    }

    public static boolean isDecimal(String s) {
        return Pattern.compile(Patterns.DOUBLE_FORMAT).matcher(s).find();
    }

    public static boolean isString(String s) {
        return Pattern.compile(Patterns.STRING_FORMAT).matcher(s).find();
    }

    public static String getNumberValue(String s) {
//        String number = deleteComma(s);
//        if (Pattern.compile(Patterns.NUMBER_FORMAT).matcher(deleteComma(s)).matches()) {
//            return number;
//        } else return s;
        s = s.substring(0, s.indexOf(",")).trim();
        return Pattern.compile(Patterns.NUMBER_FORMAT).matcher(s)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElse("null");
    }

    public static String getBooleanValue(String s) {
//        if (Pattern.compile(Patterns.BOOLEAN_FORMAT).matcher(s).matches()) {
//            return s.toLowerCase();
//        } else return null;
        s = s.substring(0, s.indexOf(",")).trim();
        return Pattern.compile(Patterns.BOOLEAN_FORMAT).matcher(s)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElse("null");
    }

    public static String getArrayValue(String s) {
        if (Pattern.compile(Patterns.ARRAY_FORMAT).matcher(s).matches()) {
            StringBuilder builder = new StringBuilder();
            int brackets = 0;
            for (char c : s.toCharArray()) {
                builder.append(c);

                if (c == '[') brackets++;
                if (brackets == ']') brackets--;
                if (brackets == 0) break;
            }
            return builder.toString();
        } else return "null";
    }

    public static String getStringValue(String s) {
        StringBuilder builder = new StringBuilder();
        int quotes = 0;

        for (char c : s.toCharArray()) {
            if (quotes > 0) builder.append(c);
            if (c == '"') quotes++;
            if (quotes == '"') quotes--;
            if (quotes == 0) break;
        }

        return builder.toString();
//        s = s.substring(0, s.indexOf(",")).trim();
//        return Pattern.compile(Patterns.STRING_FORMAT).matcher(s)
//                .results()
//                .map(MatchResult::group)
//                .findFirst()
//                .orElse("null");
    }

    public static String cutBrackets(String s, String leftBracket, String rightBracket) {
        if (s.startsWith(leftBracket) && s.endsWith(rightBracket)) {       // to get content in {} brackets
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }

    public static String getCharValue(String jsonData) {
        return "'" + jsonData.substring(1, 2) + "'";
    }

    public static boolean isChar(String value) {
        return (value.startsWith("'") && value.endsWith("'"));
    }


//    public static String getValue(String s) {
//
//    }
}
