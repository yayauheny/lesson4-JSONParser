package ru.clevertec.json.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.json.entity.Person;
import ru.clevertec.json.exception.JSONParsingException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JSONParserTest {
    JSONParser parser;

    @BeforeEach
    void setUp() {
        parser = JSONParser.getInstance();
    }

    @Nested
    @DisplayName("class for testing toJson() method")
    class ObjectToJsonTest {
        @DisplayName("check parsing object to json correct")
        @Test
        void checkWriteObjectToJsonCorrectly() {
            Person personTest = new Person(35, "Alex", true, '[', new double[]{1.0, 4.02304, 543.543, 4.5, 5.23});
            String expectedData = "{\"age\":35,\"name\":\"Alex\",\"isunemployed\":true,\"keyword\":\"[\",\"array\":[1.0,4.02304,543.543,4.5,5.23],\"child\":null}";
            String actualData = parser.toJSON(personTest);

            assertThat(actualData).isEqualTo(expectedData);
        }

        @DisplayName("check parsing object with null fields to json")
        @Test
        void checkWriteObjectToJsonWithNullableFields() {
            Person personTest = new Person(30, null, false, '`', null);
            String expected = "{\"age\":30,\"name\":null,\"isunemployed\":false,\"keyword\":\"`\",\"array\":null,\"child\":null}";
            String actual = parser.toJSON(personTest);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("check parsing object with infinity or NaN")
        @Test
        void checkWriteNanOrInfinity() {
            Person personTest = new Person(Integer.MAX_VALUE, null, false, '`', new double[]{Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY});
            String expected = "{\"age\":2147483647,\"name\":null,\"isunemployed\":false,\"keyword\":\"`\",\"array\":[NaN,-Infinity,Infinity],\"child\":null}";
            String actual = parser.toJSON(personTest);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("check parsing object return null for inner object")
        @Test
        void checkWriteObjectToJsonWithInnerObjectReturnNull() {
            Person innerPersonTest = new Person(4, "Smith", true, 'c', null);
            Person personTest = new Person(45, "Alex", false, 'f', null, innerPersonTest);
            String expected = "{\"age\":45,\"name\":\"Alex\",\"isunemployed\":false,\"keyword\":\"f\",\"array\":null,\"child\":null}";
            String actual = parser.toJSON(personTest);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("check parsing arrays return empty json")
        @ParameterizedTest
        @MethodSource("ru.clevertec.json.parser.JSONParserTest$ObjectToJsonTest#getStringArrays")
        void checkWriteStringArraysToJson(Object[] array) {
            String expected = "{}";
            String actual = parser.toJSON((Object) array);

            assertThat(actual).isEqualTo(expected);
        }

        static Stream<Arguments> getStringArrays() {
            return Stream.of(
                    Arguments.of((Object) new String[]{"One", "two", "three", "four"}),
                    Arguments.of((Object) new Boolean[]{true, false, false, true}),
                    Arguments.of((Object) new Double[]{2.0, 3.0, 4.0, 5.0}),
                    Arguments.of((Object) new Integer[]{2, 3, 4, 5, 6, 7})
            );
        }

    }

    @Nested
    @DisplayName("class for testing fromJson() method")
    class JsonToObjectTest {
        @DisplayName("check parsing json to object correctly")
        @Test
        void checkParseJsonToObjectCorrectly() {
            String source = "{\"age\":35,\"name\":\"Alex\",\"isunemployed\":true,\"keyword\":\"[\",\"array\":[1.0,4.02304,543.543,4.5,5.23]}";
            Person expectedPerson = new Person(35, "Alex", true, '[', new double[]{1.0, 4.02304, 543.543, 4.5, 5.23});
            Person actualPerson = parser.fromJSON(source, Person.class);

            assertThat(actualPerson.toString()).isEqualTo(expectedPerson.toString());
        }

        @DisplayName("check parsing object with Nan or infinity throws exception")
        @Test
        void checkParseNanAndInfinityToObjectThrowsJSONParsingException() {
            String source = "{\"age\":35,\"name\":\"Alex\",\"isunemployed\":true,\"keyword\":\"[\",\"array\":[NaN,-Infinity,Infinity]}";

            assertThrows(JSONParsingException.class, () -> parser.fromJSON(source, Person.class));
        }

        @DisplayName("check parsing object with Nan or infinity throws exception")
        @Test
        void checkParseJsonWithNullableFieldsReturnObject() {
            String source = "{\"age\":20,\"name\":\"null\",\"isunemployed\":true,\"keyword\":\"[\",\"array\":null}";
            String actual = parser.fromJSON(source, Person.class).toString();
            String expected = new Person(20, null, true, '[', null).toString();

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("check parsing json with incorrect data throws runtime exception")
        @Test
        void checkParseJsonWithIncorrectDataThrowsRuntimeException() {
            String source = "{\"age\":20,\"name\":\"null\",\"isunemployed\":fasd,\"keyword\":\"[\",\"array\":{sdfsdf}}";

            assertThrows(RuntimeException.class, () -> parser.fromJSON(source, Person.class));
        }
    }
}
