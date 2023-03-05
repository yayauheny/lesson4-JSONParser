package ru.clevertec.json.parser;

import java.io.File;

public interface JSONTemplate {
    <T>T fromJSON(File file, Class<T> objects);
    <T>T fromJSON(String str, Class<T> objects);
    void toJSON(File file, Object... objects);
    String toJSON(Object...objects);
}
