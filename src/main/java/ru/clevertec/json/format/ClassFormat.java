package ru.clevertec.json.format;

import ru.clevertec.json.exception.JSONParsingException;
import ru.clevertec.json.parser.JSONDeserializer;
import ru.clevertec.json.util.ReflectionUtil;
import ru.clevertec.json.util.StringUtil;

public final class ClassFormat implements ClassFormatTemplate {
    private static ClassFormat INSTANCE;
    private static ReflectionUtil reflectionUtil = ReflectionUtil.getInstance();
    private static StringUtil stringUtil = StringUtil.getInstance();

    public <T> T checkValueCompatibility(String value, Class<T> clazz) {
        if (isNull(value)) return null;
        else if (isInteger(clazz) && stringUtil.isNumber(value)) {
            return (T) Integer.valueOf(value);
        } else if (isDecimal(clazz) && stringUtil.isDecimal(value)) {
            return (T) Double.valueOf(value);
        } else if (isString(clazz)) {
            return (T) value;
        } else if (isChar(clazz)) {
            return (T) Character.valueOf(value.charAt(0));
        } else if (isBoolean(clazz) && stringUtil.isBoolean(value)) {
            return (T) Boolean.valueOf(value.substring(1, 2));
        } else if (isObject(clazz) && stringUtil.isObject(value)) {
            return (T) JSONDeserializer.getInstance().fromJSON(value, clazz);
        } else if (isArray(clazz) && stringUtil.isArrayValue(value)) {
            return (T) reflectionUtil.parseArray(value, clazz);
        } else throw new JSONParsingException(String.format("Exception parse value: %s", value));
    }

    public boolean isInteger(Class clazz) {
        if (clazz == Integer.class ||
                clazz == Byte.class ||
                clazz == Long.class ||
                clazz == Short.class ||
                clazz == int.class ||
                clazz == byte.class ||
                clazz == long.class ||
                clazz == short.class
        ) {
            return true;
        } else return false;
    }

    public boolean isDecimal(Class clazz) {
        if (clazz == Double.class ||
                clazz == Float.class ||
                clazz == float.class ||
                clazz == double.class
        ) {
            return true;
        } else return false;
    }

    public boolean isString(Class clazz) {
        return clazz == String.class;
    }

    public boolean isChar(Class clazz) {
        return (clazz == Character.class || clazz == char.class);
    }

    public boolean isArray(Class clazz) {
        return clazz.isArray();
    }

    public boolean isBoolean(Class clazz) {
        return (clazz == Boolean.class || clazz == boolean.class);
    }

    public boolean isObject(Class clazz) {
        return clazz == Object.class;
    }

    public boolean isNull(String s) {
        return "null".equalsIgnoreCase(s);
    }

    private ClassFormat() {
    }

    public static ClassFormat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassFormat();
        }
        return INSTANCE;
    }
}
