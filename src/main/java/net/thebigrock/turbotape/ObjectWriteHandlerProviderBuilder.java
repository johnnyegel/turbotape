package net.thebigrock.turbotape;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds an ObjectWriteHandlerProvider, which simply keeps a map of
 * what ObjectWriteHandlers to use to process an object of a given class.
 */
public class ObjectWriteHandlerProviderBuilder {
    private final Map<Class<?>, Entry<?>> _classEntryMap = new HashMap<>();

    /**
     * Private constructor. Use the {@link #create()} method to create a builder
     */
    private ObjectWriteHandlerProviderBuilder() { }

    public static ObjectWriteHandlerProviderBuilder create() {
        return new ObjectWriteHandlerProviderBuilder();
    }

    /**
     * Adds a handler for objects of a specified class
     * @param alias The alias to use to reference the objects class
     * @param cls The class of the object written by the handler
     * @param writeHandler The write handler for the object
     * @param <T> The type of the object
     * @return This ObjectWriteHandlerProviderBuilder instance for cascading
     */
    public <T> ObjectWriteHandlerProviderBuilder add(String alias, Class<T> cls, ObjectWriteHandler<T> writeHandler) {
        _classEntryMap.put(cls, new Entry<>(alias, writeHandler));
        return this;
    }

    /**
     * Adds a handler for objects of a specified class
     *
     * The {@link Class#getSimpleName()} is used as the class alias
     *
     * @param cls The class of the object written by the handler
     * @param writeHandler The write handler for the object
     * @param <T> The type of the object
     * @return This ObjectWriteHandlerProviderBuilder instance for cascading
     */
    public <T> ObjectWriteHandlerProviderBuilder add(Class<T> cls, ObjectWriteHandler<T> writeHandler) {
        _classEntryMap.put(cls, new Entry<>(cls.getSimpleName(), writeHandler));
        return this;
    }

    /**
     * Builds the ObjectWriteHandlerProvider
     * @return ObjectWriteHandlerProvider instance
     */
    public ObjectWriteHandlerProvider build() {
        return new Provider(_classEntryMap);
    }

    /**
     * Internal entry which holds the Alias and Write Handler
     * @param <T> The type handled by the Write Handler
     */
    private static class Entry<T> {
        private final String _alias;
        private final ObjectWriteHandler<T> _writeHandler;

        private Entry(String alias, ObjectWriteHandler<T> writeHandler) {
            _alias = alias;
            _writeHandler = writeHandler;
        }
    }

    /**
     * The provider implementation this builder creates
     */
    private static class Provider implements ObjectWriteHandlerProvider {
        private final Map<Class<?>, Entry<?>> _classEntryMap;

        private Provider(Map<Class<?>, Entry<?>> classEntryMap) {
            _classEntryMap = Collections.unmodifiableMap(classEntryMap);
        }

        @Override
        public <T> ObjectWriteHandler<T> getWriter(Class<T> cls) {
            return get(cls)._writeHandler;
        }

        @Override
        public <T> String getAlias(Class<T> cls) {
            return get(cls)._alias;
        }

        @SuppressWarnings("unchecked")  // Unchecked Cast is safe, as we enforce type match on build
        private <T> Entry<T> get(Class<T> cls) {
            if (_classEntryMap.containsKey(cls)) return (Entry<T>) _classEntryMap.get(cls);
            throw new IllegalStateException("No ObjectWriteHandler registered for class: " + cls);
        }
    }
}
