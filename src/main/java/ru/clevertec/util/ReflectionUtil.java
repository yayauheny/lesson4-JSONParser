package ru.clevertec.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ReflectionUtil {
    public static List<Field> getAllDeclaredFields(Class<?> aClass) {
        if (aClass == null) return Collections.emptyList();
        final Class<?> superclass = aClass.getSuperclass();
        return Stream.concat(Arrays.stream(aClass.getDeclaredFields()), getAllDeclaredFields(superclass).stream())
                .toList();
    }


}
