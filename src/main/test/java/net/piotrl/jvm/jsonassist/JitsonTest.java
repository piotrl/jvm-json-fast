package net.piotrl.jvm.jsonassist;

import junit.framework.TestCase;
import net.piotrl.jvm.jsonassist.mock.CollectionPrimitives;
import net.piotrl.jvm.jsonassist.mock.NestedObject;
import net.piotrl.jvm.jsonassist.mock.PlainPrimitives;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JitsonTest extends TestCase {

    public void testNullConversion() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        Object object = null;

        // act
        String json = jsonSerializer.toJson(object);

        // assert
        assertThat(json).isEqualTo("null");
    }

    public void testPrimitiveObject() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        PlainPrimitives object = mockPlainPrimitives(256, 1L, "Hey jude");

        // act
        String json = jsonSerializer.toJson(object);

        // assert
        assertThat(json).isEqualTo(
                "{number: 256, longNumber: 1, string: \"Hey jude\", active: true}"
        );
    }

    public void testNestedObject() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        PlainPrimitives object = mockPlainPrimitives(256, 1L, "Hey jude");
        NestedObject nestedObject = new NestedObject();
        nestedObject.setId(13);
        nestedObject.setPlainPrimitives(object);

        // act
        String json = jsonSerializer.toJson(nestedObject);

        // assert
        assertThat(json).isEqualTo(
                "{id: 13, plainPrimitives: {number: 256, longNumber: 1, string: \"Hey jude\", active: true}}"
        );
    }

    public void testPrimitiveCollection() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        CollectionPrimitives collectionPrimitives = new CollectionPrimitives();
        collectionPrimitives.setList(Arrays.asList(1, 2, 3, 4, 1337));

        // act
        String json = jsonSerializer.toJson(collectionPrimitives);

        // assert
        assertThat(json).isEqualTo(
                "{list: [1, 2, 3, 4, 1337]}"
        );
    }

    public void testCollectionOfPrimitives() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        List<Number> listOfNumbers = Arrays.asList(1, 2, 3, 4, 1337);
        List<String> listOfStrings = Arrays.asList("hey", "go", "sleep", "it's", "1am");

        // act
        String jsonArrayNumbers = jsonSerializer.toJson(listOfNumbers);
        String jsonArrayString = jsonSerializer.toJson(listOfStrings);

        // assert
        assertThat(jsonArrayNumbers).isEqualTo("[1, 2, 3, 4, 1337]");
        assertThat(jsonArrayString).isEqualTo("[\"hey\", \"go\", \"sleep\", \"it's\", \"1am\"]");
    }

    public void testCollectionOfObjects() throws Exception {
        // arrange
        Jitson jsonSerializer = new Jitson();
        List<PlainPrimitives> listOfObjects = Arrays.asList(
                mockPlainPrimitives(1, 2L, "First"),
                mockPlainPrimitives(3, 4L, "Second")
        );

        // act
        String json = jsonSerializer.toJson(listOfObjects);

        // assert
        assertThat(json).isEqualTo(
                "[{number: 1, longNumber: 2, string: \"First\", active: true}, {number: 3, longNumber: 4, string: \"Second\", active: true}]"
        );
    }

    private PlainPrimitives mockPlainPrimitives(int number, long longNumber, String s) {
        PlainPrimitives object = new PlainPrimitives();
        object.setNumber(number);
        object.setLongNumber(longNumber);
        object.setString(s);
        object.setActive(true);

        return object;
    }
}