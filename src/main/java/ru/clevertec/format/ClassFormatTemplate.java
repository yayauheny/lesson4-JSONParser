package ru.clevertec.format;

public interface ClassFormatTemplate {
    <T> T checkValueCompatibility (String jsonData, Class<T> clazz);
}
