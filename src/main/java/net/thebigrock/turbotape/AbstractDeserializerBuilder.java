package net.thebigrock.turbotape;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Builds a deserializer by registering deserializing methods for each type
 * which should be handled by the Deserializer.
 */
public abstract class AbstractDeserializerBuilder {
    private final Map<Class<?>, Consumer<FieldWriter>> _writerClassMap = new HashMap<>();

    /**
     * Adds a serialization handler for a specified class
     * @param cls The class the handler will serialize
     * @param handler The handler method
     */
    public AbstractDeserializerBuilder add(Class<?> cls, Consumer<FieldWriter> handler) {
        if (_writerClassMap.containsKey(cls)) {
            Consumer<FieldWriter> currentHandler = _writerClassMap.get(cls);
            throw new IllegalArgumentException("Class [" + cls + "] is already handled by: " + currentHandler);
        }
        _writerClassMap.put(cls, handler);
        return this;
    }

    /**
     * Builds the Object serializer from the registered serializer handlers
     * @return The resulting object serializer
     */
    public abstract Serializer build();
}
