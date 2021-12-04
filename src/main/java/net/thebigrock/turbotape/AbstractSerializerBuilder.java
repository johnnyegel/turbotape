package net.thebigrock.turbotape;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds a serializer by registering deserializing methods for each type
 * which should be handled by the Deserializer.
 */
public abstract class AbstractSerializerBuilder {
    private final Map<String, String> classAliasMap = new HashMap<>();
    protected final Map<Class<?>, ClassAlias<?>> classSerializerMap = new HashMap<>();

    /**
     * Keeps the class alias and writer
     */
    public static class ClassAlias<T> {
        private final String alias;
        private final ObjectWriter<T> objectWriter;

        private ClassAlias(String name, ObjectWriter<T> objectWriter) {
            this.alias = name;
            this.objectWriter = objectWriter;
        }

        public String alias() {
            return this.alias;
        }

        public ObjectWriter<T> writer() {
            return this.objectWriter;
        }
    }

    /**
     * Adds a serialization handler for a specified class
     *
     * @param cls The class the handler will serialize
     * @param handler The handler method
     * @param <T> The type of the object being serialized
     * @return This Serialization builder for cascading
     */
    public <T> AbstractSerializerBuilder add(Class<T> cls, ObjectWriter<T> handler) {
        return add(cls.getName(), cls, handler);
    }

    /**
     * Adds a serialization handler for a specified class and specifies a custom name to identify the type
     *
     * This is used to reduce the size of the output, by using the given name instead of object class names.
     *
     * @param alias The alias to use to identify the class of the objects
     * @param cls The class to serialize
     * @param handler The handler
     * @param <T> The type of the object being serialized
     * @return This Serialization builder for cascading
     */
    public <T> AbstractSerializerBuilder add(String alias, Class<T> cls, ObjectWriter<T> handler) {
        if (classSerializerMap.containsKey(cls)) {
            throw new IllegalArgumentException("Class [" + cls + "] is already handled by: "
                    + classSerializerMap.get(cls));
        }
        if (classAliasMap.containsKey(alias)) {
            throw new IllegalArgumentException("Class alias [" + alias + "] already assigned to class: "
                    + classAliasMap.get(alias));
        }
        classAliasMap.put(alias, cls.getName());
        classSerializerMap.put(cls, new ClassAlias<>(alias, handler));
        return this;
    }

    /**
     * Builds the Object serializer from the registered serializer handlers
     * @return The resulting object serializer
     */
    public abstract Serializer build();
}
