package net.thebigrock.turbotape;

/**
 * Interface for handler method that reads data for an object and then creates an instance of it
 */
public interface ObjectReadHandler<T> {

    /**
     * Builds an instance of object T from the given reader
     * @param reader The field reader to read object data from
     * @return The constructed object
     */
    T build(FieldReader reader);
}
