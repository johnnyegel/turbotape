package net.thebigrock.turbotape;

/**
 * Lambda interface defining a Method which handles writing of an objects fields to a FieldWriter
 */
public interface ObjectWriter {

    /**
     * Writes the objects field using the provided Field Writer
     * @param writer The writer
     */
    <T> void writeObject(FieldWriter writer, T object);
}
