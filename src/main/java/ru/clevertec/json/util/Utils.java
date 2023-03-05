package ru.clevertec.json.util;

public final class Utils {
    private static Utils INSTANCE;
    public boolean isNull(Object obj) {
        return (obj == null || obj.equals("null"));
    }

    public boolean isTextValue(Object obj) {
        return (obj instanceof String || obj instanceof Character
                || obj.getClass() == char.class);
    }

    public boolean isNumber(Object obj) {
        return (obj instanceof Number || obj.getClass().isPrimitive() &&
                obj.getClass() != char.class && obj.getClass() != boolean.class);
    }

    private Utils() {
    }

    public static Utils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Utils();
        }
        return INSTANCE;
    }
}
