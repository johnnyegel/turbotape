package net.thebigrock.turbotape;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Interface defining the method provided by an Object deserializer
 */
public interface Deserializer {
    /**
     * Deserializes the specified object from the given input stream
     * @param cls The class of the object to deserialize
     * @param inputStream The input stream
     * @param <T> The object type deferred from the specified class
     * @return The deserialized object
     */
    <T> T deserialize(Class<T> cls, InputStream inputStream);

    /**
     * Deserializes the specified object from the given byte array
     * @param cls The class of the object to deserialize
     * @param bytes The byte data to deserialize
     * @param <T> The object type deferred from the specified class
     * @return The deserialized object
     */
    default <T> T deserialize(Class<T> cls, byte[] bytes) {
        return deserialize(cls, new ByteArrayInputStream(bytes));
    }
}
