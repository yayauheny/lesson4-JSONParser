package ru.clevertec.json.parser;

import java.io.File;

public final class JSONParser implements JSONTemplate {
    private static JSONParser INSTANCE;
    private static JSONDeserializer deserializer;
    private static JSONSerializer serializer;

    @Override
    public <T> T fromJSON(File file, Class<T> object) {
        deserializer = JSONDeserializer.getInstance();
        return deserializer.fromJSON(file, object);
    }

    @Override
    public <T> T fromJSON(String str, Class<T> object) {
        deserializer = JSONDeserializer.getInstance();
        return deserializer.fromJSON(str, object);
    }

    @Override
    public void toJSON(File file, Object... objects) {
        serializer = JSONSerializer.getInstance();
        serializer.toJSON(file, objects);
    }

    @Override
    public String toJSON(Object... objects) {
        serializer = JSONSerializer.getInstance();
        return serializer.toJSON(objects);
    }

    private JSONParser() {
    }

    public static JSONParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JSONParser();
        }
        return INSTANCE;
    }
}
