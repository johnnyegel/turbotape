package net.thebigrock.turbotape;

/**
 * Defines Lambda interface for a function which writes the fields of an object to a field writer
 * @param <T> The object type this specific field writer handles
 */
@FunctionalInterface
public interface ObjectFieldWriter<T> {

    /**
     * Writes all (relevant) fields of an object to the field writer
     * @param object The object to write
     * @param fieldWriter The field writer to write to
     */
    void write(T object, FieldWriter fieldWriter);
}
