package net.thebigrock.turbotape;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Builds a serializer by registering deserializing methods for each type
 * which should be handled by the Deserializer.
 */
public abstract class AbstractSerializerBuilder {
    protected final Map<Class<?>, ObjectWriter> classSerializerMap = new HashMap<>();

    /**
     * Adds a serialization handler for a specified class
     * @param cls The class the handler will serialize
     * @param handler The handler method
     */
    public AbstractSerializerBuilder add(Class<?> cls, ObjectWriter handler) {
        if (classSerializerMap.containsKey(cls)) {
            throw new IllegalArgumentException("Class [" + cls + "] is already registered for handling");
        }
        classSerializerMap.put(cls, handler);
        return this;
    }

    /**
     * Builds the Object serializer from the registered serializer handlers
     * @return The resulting object serializer
     */
    public abstract Serializer build();

}
