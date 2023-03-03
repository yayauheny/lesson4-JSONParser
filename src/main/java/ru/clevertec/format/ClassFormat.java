package ru.clevertec.format;

import ru.clevertec.exception.JSONParsingException;
import ru.clevertec.parser.JSONParser;
import ru.clevertec.util.Utils;

public class ClassFormat implements ClassFormatTemplate {
    private static ClassFormat INSTANCE;

    public static ClassFormat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassFormat();
        }
        return INSTANCE;
    }

    public <T> T checkValueCompatibility(String value, Class<T> clazz) {
        if (isNull(value)) return null;
        else if (isInteger(clazz) && Utils.isNumber(value)) {
            return (T) Integer.valueOf(value);
        } else if (isDecimal(clazz) && Utils.isDecimal(value)) {
            return (T) Double.valueOf(value);
        }
        else if (isString(clazz)) {
            return (T) value;
        }
        else if (isChar(clazz)){
            return (T) Character.valueOf(value.charAt(0));
        }
        else if (isBoolean(clazz) && Utils.isBoolean(value)) {
            return (T) Boolean.valueOf(value.substring(1,2));
        }
        else if (isObject(clazz) && Utils.isObject(value)) {
            return (T) new JSONParser().fromJSON(value, clazz);
        }
        else if (isArray(clazz) && Utils.isArrayValue(value)) {    //заглушка на время
            return (T) null;
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
}
