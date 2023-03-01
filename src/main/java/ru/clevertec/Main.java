package ru.clevertec;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.parser.JSONParser;
import ru.clevertec.util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchFieldException {
        double[][] array = {{1.0, 2.0}, {3.5, 1.23123}, {1.23213124125, 1}, {13213}};
        Person person = new Person(20, "Mike", true, 's', array);
        JSONParser parser = new JSONParser();
        File myVersion = Path.of("src\\main\\java\\resources\\personsMy.json").toFile();
        File example = Path.of("src\\main\\java\\resources\\personsExample.json").toFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(example, person);

        parser.toJSON(myVersion, person, person, person, person);
    }


    public static class Person {
        private int age;
        private String name;
        private boolean isUnemployed;
        private char keyWord;
        private double[][] array;

        public double[][] getArray() {
            return array;
        }

        public Person() {
        }

        public void setArray(double[][] array) {
            this.array = array;
        }

        public Person(int age, String name, boolean isUnemployed, char keyWord, double[][] array) {
            this.age = age;
            this.name = name;
            this.isUnemployed = isUnemployed;
            this.keyWord = keyWord;
            this.array = array;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUnemployed() {
            return isUnemployed;
        }

        public void setUnemployed(boolean unemployed) {
            isUnemployed = unemployed;
        }

        public char getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(char keyWord) {
            this.keyWord = keyWord;
        }
    }
}