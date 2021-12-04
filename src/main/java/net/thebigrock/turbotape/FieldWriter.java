package net.thebigrock.turbotape;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Specifies methods provided by a field writer, which writes fields to a serializer
 */
public interface FieldWriter {

    /**
     * Returned from write functions to make it possible to allocate a name to serialized fields
     */
    interface Allocator {
        /**
         * Specify a string name for a field in the serialized data
         * @param name The name to give the field
         */
        void as(String name);
    }

    /**
     * Write a boolean value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(boolean value);

    /**
     * Write an integer value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(int value);

    /**
     * Write a long value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(long value);

    /**
     * Write a float value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(float value);

    /**
     * Write a double value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(double value);

    /**
     * Write a string value
     * @param value The value
     * @return Allocator for the field
     */
    Allocator write(String value);

    /**
     * Writes the specific object to the serializer
     * @param cls The class of the object to write
     * @param object The object to write
     * @param <T> The objects type
     * @return Allocator used to specify a location of the field
     */
    <T> Allocator write(Class<T> cls, T object);

    /**
     * Writes a field consisting of an Iterator of objects
     * @param cls The class provided by the iterator
     * @param objects The object stream to write
     * @param <T> The type of objects in the stream
     * @return FieldAllocator to allocate a name to the field
     */
    <T> Allocator write(Class<T> cls, Iterator<T> objects);

    /**
     * Writes a field consisting of a stream of objects
     * @param cls The class provided by the stream
     * @param objects The object stream to write
     * @param <T> The type of objects in the stream
     * @return FieldAllocator to allocate a name to the field
     */
    default <T> Allocator write(Class<T> cls, Stream<T> objects) {
        return write(cls, objects.iterator());
    }

    /**
     * Writes a field consisting of an Iterable of objects
     * @param cls The class provided by the iterator
     * @param objects The object stream to write
     * @param <T> The type of objects in the stream
     * @return FieldAllocator to allocate a name to the field
     */
    default <T> Allocator write(Class<T> cls, Iterable<T> objects) {
        return write(cls, objects.iterator());
    }
}
