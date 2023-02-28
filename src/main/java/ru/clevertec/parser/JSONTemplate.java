package ru.clevertec.parser;

import java.io.File;

public interface JSONTemplate {
    void fromJSON(File file);
    void toJSON(File file, Object...obj);
}
