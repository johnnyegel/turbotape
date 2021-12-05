package net.thebigrock.turbotape;

/**
 * Provides a Read Handler given an object type alias
 */
public interface ObjectReadHandlerProvider {

    /**
     * Returns the read handler registered for a specific alias
     * @param alias The alias registered for the read handler
     * @param <T> The type the read handler reads
     * @return The registered ObjectReadHandler
     * @throws IllegalStateException if the alias is not registered
     */
    <T> ObjectReadHandler<T> getReadHandler(String alias);
}
