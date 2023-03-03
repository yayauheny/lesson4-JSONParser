package ru.clevertec.parser;

import java.io.File;

public interface JSONTemplate {
    <T>T fromJSON(File file, Class<T> obj);
    void toJSON(File file, Object...obj);
}
