package net.thebigrock.turbotape;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface implemented by an TurboTape Object serializer.
 * Serializers should be thread safe, and must explicitly document if they are not.
 */
public interface Serializer {
    /**
     * Serializes the given object to the output stream
     * @param cls THe class of the object to serialize
     * @param object The object
     * @param outputStream The output stream
     * @param <T> The objects type
     * @throws IOException If thrown by used stream
     */
    <T> void serialize(Class<T> cls, T object, OutputStream outputStream) throws IOException;

    /**
     * Convenience default implementation that serializes an object to a byte array
     * @param cls THe class of the object to serialize
     * @param object The object to serialize
     * @param <T> The objects type
     * @return Byte array containing the serialized data
     * @throws IOException If thrown by used stream
     */
    default <T> byte[] serialize(Class<T> cls, T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serialize(cls, object, outputStream);
        return outputStream.toByteArray();
    }
}
