package net.thebigrock.turbotape;

/**
 * Lambda interface defining a Method which handles writing of an objects fields to a FieldWriter
 */
public interface ObjectWriter<T> {

    /**
     * Writes the objects field using the provided Field Writer
     * @param writer The writer
     */
    void writeObject(FieldWriter writer, T object);
}
