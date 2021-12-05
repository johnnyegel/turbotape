package net.thebigrock.turbotape;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds an ObjectReadHandlerProvider by providing alias to ReadHandler mappings
 */
public class ObjectReadHandlerProviderBuilder {
    private final Map<String, ObjectReadHandler<?>> _aliasReadHandlerMap = new HashMap<>();

    private ObjectReadHandlerProviderBuilder() { }

    /**
     * Creates the builder
     * @return The builder instance
     */
    public static ObjectReadHandlerProviderBuilder create() {
        return new ObjectReadHandlerProviderBuilder();
    }

    /**
     * Adds an alias to Read Handler mapping to the provider
     * @param alias The alias that maps to the Read Handler
     * @param readHandler The read handler producing the objects in question
     * @param <T> The type of the objects produced
     * @return The ObjectReadHandlerProviderBuilder instance for cascaded calls
     */
    public <T> ObjectReadHandlerProviderBuilder add(String alias, ObjectReadHandler<T> readHandler) {
        if (_aliasReadHandlerMap.containsKey(alias)) {
            throw new IllegalArgumentException("Handler already exists for [" + alias + "]: "
                    + _aliasReadHandlerMap.get(alias));
        }
        _aliasReadHandlerMap.put(alias, readHandler);
        return this;
    }

    /**
     * Register the class {@link Class#getSimpleName()} as an alias for the given read handler
     * @param cls The class to register a class name alias for
     * @param readHandler The read handler producing the objects in question
     * @param <T> The type of the objects produced
     * @return The ObjectReadHandlerProviderBuilder instance for cascaded calls
     */
    public <T> ObjectReadHandlerProviderBuilder add(Class<T> cls, ObjectReadHandler<T> readHandler) {
        return add(cls.getSimpleName(), readHandler);
    }

    /**
     * Internal provider implementation that wraps the reader map
     */
    private static class Provider implements ObjectReadHandlerProvider {

        private final Map<String, ObjectReadHandler<?>> _aliasReadHandlerMap;

        private Provider(Map<String, ObjectReadHandler<?>> aliasReadHandlerMap) {
            _aliasReadHandlerMap = aliasReadHandlerMap;
        }

        @Override
        public <T> ObjectReadHandler<T> getReadHandler(String alias) {
            if (_aliasReadHandlerMap.containsKey(alias)) return get(alias);
            throw new IllegalStateException("No ObjectReadHandler registered for alias: " + alias);
        }

        @SuppressWarnings("unchecked")
        private <T> ObjectReadHandler<T> get(String alias) {
            return (ObjectReadHandler<T>)_aliasReadHandlerMap.get(alias);
        }
    }
}
