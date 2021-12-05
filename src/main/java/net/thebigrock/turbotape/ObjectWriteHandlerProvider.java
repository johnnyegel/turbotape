package net.thebigrock.turbotape;

/**
 * Provides Object Write Handlers for specified objects classes.
 * The handlers in turn are used for serializing objects of that type.
 */
public interface ObjectWriteHandlerProvider {

    /**
     * Provides the writer to use for a given class
     *
     * @param cls The class of the objects to get writer for
     * @param <T> The object type to get writer for
     * @return The ObjectWriter for the specified class
     * @throws IllegalStateException if class is not supported by provider
     */
    <T> ObjectWriteHandler<T> getWriteHandler(Class<?> cls);

    /**
     * Returns alias used to represent the class by the serializer.
     *
     * If no alias is known, {@link Class#getName()} should be used.
     *
     * @param cls The class of the objects to get the alias for
     * @param <T> The object type to get alias for
     * @return The registered alias for the given class (or it's class name)
     * @throws IllegalStateException if class is not supported by provider
     */
    String getAlias(Class<?> cls);
}
