package net.thebigrock.turbotape;

/**
 * Interface for handler method that writes the data of an object to a FieldWriter.
 */
public interface ObjectWriteHandler<T> {

    /**
     * Writes the objects field using the provided Field Writer
     * @param fieldWriter The writer
     */
    void process(FieldWriter fieldWriter, T object);
}
