package net.thebigrock.turbotape;

/**
 * Defines Lambda interface for a function which reads the fields for an object from a field reader
 * @param <T> The object type this specific field reader handles
 */
@FunctionalInterface
public interface ObjectFieldReader<T> {
    /**
     * Reads all (relevant) fields of an object, then constructs and returns it
     *
     * @param cls The class of the object to read
     * @param fieldReader The field reader to read from
     */
    T read(Class<T> cls, FieldReader fieldReader);
}
