package ru.clevertec.util;

public class CharacterUtil {
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9' || c == '-';
    }

    public static boolean isString(char c) {
        return c == '"';
    }

    public static boolean isBoolean(char c) {
        return c == 't' || c == 'f' || c == 'T' || c == 'F';
    }

    public static boolean isArray(char c) {
        return c == '[' || c == ']';
    }

    public static boolean isNull(char c) {
        return c == 'n' || c == 'N';
    }

    public static boolean isChar(char c) {
        return c == '\'';
    }
}
