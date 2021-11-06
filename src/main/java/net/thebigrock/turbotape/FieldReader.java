package net.thebigrock.turbotape;

/**
 * Defines methods provided by a Field Reader, to read data from a deserializer.
 */
public interface FieldReader {
    /**
     * Sets the index to read the next field from.
     * <p>
     * If called more than once before a read is issued, an invalid state exception is raised.
     *
     * @param index The index to read from
     * @return self reference for cascading
     */
    FieldReader at(int index);

    /**
     * Equivalent to calling {@link #at(int)} with the current index
     * <p>
     * This is useful for defining an optional name for the current positional index.
     * If called more than once before a read is issued, an invalid state exception is raised.
     *
     * @return self reference for cascading
     */
    FieldReader at();

    /**
     * Specifies the name of the field reflecting the index to read
     * <p>
     * If {@link #at(int)} or has been called before this, the name is considered optional.
     * This means if it's not declared in the serialized data, the specified index is used instead.
     * {@link #at()} can be used to use the current positional field as the default.
     *
     * @param name The name for the index to read next field from
     * @return self reference for cascading
     */
    FieldReader as(String name);

    /**
     * Reads boolean from serializer
     * @return Read value
     */
    int readBool();

    /**
     * Reads integer from serializer
     * @return Read value
     */
    int readInt();

    /**
     * Reads long from serializer
     * @return Read value
     */
    long readLong();

    /**
     * Reads float from serializer
     * @return Read value
     */
    float readFloat();

    /**
     * Reads double from serializer
     * @return Read value
     */
    double readDouble();

    /**
     * Reads string from serializer
     * @return Read value
     */
    String readString();

    /**
     * Reads object of given class from serializer
     * @param cls The class of the object to read
     * @return Read object
     */
    <T> T readObject(Class<T> cls);

    /**
     * Returns iterator to read sequence of objects
     * @param cls The class of the object to read
     * @return Iterator to provide objects
     */
    <T> Iterable<T> readIterable(Class<T> cls);
}
