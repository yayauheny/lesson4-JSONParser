package ru.clevertec.json;

import ru.clevertec.json.entity.Person;
import ru.clevertec.json.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonRunner {
    public static void main(String[] args) throws IOException {
        double[] array = {1.0, 2.0, 3.5, 1.23123, 1.23213124125};
        File jsonParseFromFile = Path.of("src\\main\\java\\resources\\fromJson.json").toFile();
        File jsonParseToFile = Path.of("src\\main\\java\\resources\\toJson.json").toFile();
        String fromString = "{\"age\":20,\"name\":\"Mike\",\"isunemployed\":true,\"keyword\":\"s\",\"array\":[1.0,2.0,3.5,1.23123,1.23213124125]}";
        JSONParser parser = JSONParser.getInstance();

        Person personToJson = new Person(20, "Mike", true, 's', array);

        Person fromFile = parser.fromJSON(jsonParseFromFile, Person.class);
        Person parseFromJsonString = parser.fromJSON(fromString, Person.class);
        System.out.println("\n\nFrom .json to object: \n");
        System.out.println("Parse from JSON file: " + fromFile);
        System.out.println("Parse from string: " + parseFromJsonString);

        parser.toJSON(jsonParseToFile, personToJson);
        String toString =  parser.toJSON(personToJson);
        String toFile = Files.readString(jsonParseToFile.toPath());
        System.out.println("\n\nFrom object to .json: \n");
        System.out.println("Parse to JSON file: " + toFile);
        System.out.println("Parse to string: " + toString);
    }
}
