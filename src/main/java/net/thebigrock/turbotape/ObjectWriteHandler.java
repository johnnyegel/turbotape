package net.thebigrock.turbotape;

/**
 *
 */
public interface ObjectWriteHandler<T> {

    /**
     * Writes the objects field using the provided Field Writer
     * @param fieldWriter The writer
     */
    void writeObject(FieldWriter fieldWriter, T object);
}
