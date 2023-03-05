package ru.clevertec.json.util;

public final class CharacterUtil {
    private static CharacterUtil INSTANCE;
    public boolean isNumber(char c) {
        return c >= '0' && c <= '9' || c == '-';
    }

    public boolean isString(char c) {
        return c == '"';
    }

    public boolean isBoolean(char c) {
        return c == 't' || c == 'f' || c == 'T' || c == 'F';
    }

    public boolean isArray(char c) {
        return c == '[' || c == ']';
    }

    private CharacterUtil() {
    }

    public static CharacterUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CharacterUtil();
        }
        return INSTANCE;
    }
}
