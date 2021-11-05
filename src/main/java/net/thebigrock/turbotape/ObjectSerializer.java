package net.thebigrock.turbotape;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Interface implemented by an TurboTape Object serializer.
 * Serializers should be thread safe, and must explicitly document if they are not.
 */
public interface ObjectSerializer {
    /**
     * Serializes the given object to the output stream
     * @param object The object
     * @param outputStream The output stream
     * @param <T> The objects type
     */
    <T> void serialize(T object, OutputStream outputStream);

    /**
     * Convenience default implementation that serializes an object to a byte array
     * @param object The object to serialize
     * @param <T> The objects type
     * @return Byte array containing the serialized data
     */
    default <T> byte[] serialize(T object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serialize(object, outputStream);
        return outputStream.toByteArray();
    }
}
