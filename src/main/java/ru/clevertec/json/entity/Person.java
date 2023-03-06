package ru.clevertec.json.entity;

import java.util.Arrays;

public class Person {
    private int age;
    private String name;
    private boolean isUnemployed;
    private char keyWord;
    private double[] array;

    public double[] getArray() {
        return array;
    }

    public Person getChild() {
        return child;
    }

    public void setChild(Person child) {
        this.child = child;
    }

    public Person child;

    public Person(int age, String name, boolean isUnemployed, char keyWord, double[] array, Person child) {
        this.age = age;
        this.name = name;
        this.isUnemployed = isUnemployed;
        this.keyWord = keyWord;
        this.array = array;
        this.child = child;
    }

    public Person() {
    }

    public void setArray(double[] array) {
        this.array = array;
    }

    public Person(int age, String name, boolean isUnemployed, char keyWord, double[] array) {
        this.age = age;
        this.name = name;
        this.isUnemployed = isUnemployed;
        this.keyWord = keyWord;
        this.array = array;
    }

    public Person(int age, String name, boolean isUnemployed, char keyWord) {
        this.age = age;
        this.name = name;
        this.isUnemployed = isUnemployed;
        this.keyWord = keyWord;
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

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", isUnemployed=" + isUnemployed +
                ", keyWord=" + keyWord +
                ", array=" + Arrays.toString(array) +
                '}';
    }

}