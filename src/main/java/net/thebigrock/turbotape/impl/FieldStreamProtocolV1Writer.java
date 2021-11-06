package net.thebigrock.turbotape.impl;

import net.thebigrock.turbotape.FieldWriter;
import net.thebigrock.turbotape.ObjectWriter;
import net.thebigrock.turbotape.Serializer;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * Writer supporting the FieldStreamProtocol V1
 *
 * This writer has a limit of 4096 unique field and type names (total combined).
 * It should be enough for most purposes, but it's worth noting.
 *
 * NOTE! This is a proprietary data encoding protocol specific to this project.
 *
 * - u16 - Field header
 *   - Bit 0-3 - Encode the field type
 *   - Bit 4-31 - Field/Type name index
 * - The first time a Field/Type Header is seen, a UTF string containing the name is expected next.
 *   NOTE! Names are only added once, and will not be outputted again.
 * - The field content
 *
 */
public class FieldStreamProtocolV1Writer implements FieldWriter, Serializer {
    private static final String PROTOCOL_HEADER = "FSP1";
    private static final int TYPE_FLAG_BOOLEAN_FALSE = 0x00;
    private static final int TYPE_FLAG_BOOLEAN_TRUE = 0x01;
    private static final int TYPE_FLAG_INTEGER_32 = 0x02;
    private static final int TYPE_FLAG_INTEGER_64 = 0x03;
    private static final int TYPE_FLAG_FLOAT_32 = 0x04;
    private static final int TYPE_FLAG_FLOAT_64 = 0x05;
    private static final int TYPE_FLAG_UTF_STRING = 0x06;
    private static final int TYPE_FLAG_REF_OBJECT = 0x08;
    private static final int TYPE_FLAG_REF_ARRAY = 0x09;

    private final Deque<FieldEntry> fieldFifo = new LinkedList<>();
    private final Deque<IOConsumer<DataOutput>> objectFifo = new LinkedList<>();

    private final Map<String, Integer> fieldIndexMap = new HashMap<>();
    private final Map<String, Integer> typeIndexMap = new HashMap<>();

    private final Map<Class<?>, ObjectWriter> writerClassMap;

    private int nextFieldIndex = 1;
    private int nextTypeIndex = 1;

    public FieldStreamProtocolV1Writer(
            Map<Class<?>, ObjectWriter> writerClassMap) {
        this.writerClassMap = Collections.unmodifiableMap(writerClassMap);
    }


    @Override
    public <T> void serialize(T object, OutputStream outputStream) throws IOException {
        // Get the data output, and write the Protocol ID string
        DataOutput out = new DataOutputStream(outputStream);
        out.writeBytes(PROTOCOL_HEADER);

        // Add the initial object entry to the queue
        addObject(o -> processObjectEntry(o, object));
    }

    @Override
    public Allocator write(boolean value) {
        return addField(new TokenField(value ? TYPE_FLAG_BOOLEAN_TRUE : TYPE_FLAG_BOOLEAN_FALSE));
    }

    @Override
    public Allocator write(int value) {
        return addField(new DataOutputField(TYPE_FLAG_INTEGER_32, out -> out.writeInt(value)));
    }

    @Override
    public Allocator write(long value) {
        return addField(new DataOutputField(TYPE_FLAG_INTEGER_64, out -> out.writeLong(value)));
    }

    @Override
    public Allocator write(float value) {
        return addField(new DataOutputField(TYPE_FLAG_FLOAT_32, out -> out.writeFloat(value)));
    }

    @Override
    public Allocator write(double value) {
        return addField(new DataOutputField(TYPE_FLAG_FLOAT_64, out -> out.writeDouble(value)));
    }

    @Override
    public Allocator write(String value) {
        return addField(new DataOutputField(TYPE_FLAG_UTF_STRING, out -> out.writeUTF(value)));
    }

    @Override
    public <T> Allocator write(T object) {
        Allocator entry = addField(new TokenField(TYPE_FLAG_REF_OBJECT));
        // FIXME! The object needs to be queued for writing after the content of the current
        return entry;
    }

    @Override
    public <T> Allocator write(Iterator<T> objects) {
        Allocator entry = addField(new TokenField(TYPE_FLAG_REF_ARRAY));
        // FIXME! THe object needs to queued for writing after the current content
        return entry;
    }

    /**
     * Convenience method to add a Field Entry and then return the
     * @param f the entry to add
     * @return The entry, as an Allocator
     */
    private Allocator addField(FieldEntry f) {
        fieldFifo.addFirst(f);
        return f;
    }

    /**
     * Convenience method to add an object entry to the fifo
     * @param o The object entry to add
     */
    private void addObject(IOConsumer<DataOutput> o) {
        objectFifo.addFirst(o);
    }

    /**
     * Gets the writer for the given class name
     * @param cls The class name to handle
     * @param <T> The type
     * @return Object Writer
     * @throws IllegalArgumentException If class is not registered
     */
    private <T> ObjectWriter getWriter(Class<T> cls) {
        if (!writerClassMap.containsKey(cls)) {
            throw new IllegalArgumentException("No writer registered for class [" + cls+ "]");
        }
        return writerClassMap.get(cls);
    }

    /**
     * Processes an object entry and writes it th the output
     * @param out The output to write to
     * @param object The object to process
     * @param <T> The type of the object
     */
    private <T> void processObjectEntry(DataOutput out, T object) throws IOException {
        // Get the writer delegate, and ask it to write the object
        Class<? extends Object> cls = object.getClass();
        ObjectWriter writer = getWriter(cls);
        String typeName = cls.getName();

        // :? Does the type name have an index already?
        if (!typeIndexMap.containsKey(typeName)) {
            // -> No, make sure there is an index available
            if (nextFieldIndex >= Short.MAX_VALUE) {
                throw new IllegalStateException("Max unique field/type name index count of 4096 exceeded.");
            }
            // Output the index value, then the type name
            out.writeShort(nextTypeIndex);
            out.writeUTF(typeName);
            // Make sure the same index is used next time
            typeIndexMap.put(typeName, nextFieldIndex++);
        }
        else {
            // -> Yes, simply write the index
            out.writeShort(typeIndexMap.get(typeName));
        }

        // :: Delegate the object field writing
        writer.writeObject(this, object);

        // Before outputting data, write out the number of fields to expect
        out.writeShort(fieldFifo.size());

        // :: Process the fields until the field list is empty
        while (!fieldFifo.isEmpty()) {
            // Get the next field to output
            fieldFifo.removeLast().write(out);
        }

    }

    /**
     * Processes an object entry and writes it th the output
     * @param out The output to write to
     * @param iterator The iterator to process
     * @param <T> The type of the object being iterated
     */
    private <T> void processIteratorEntry(DataOutput out, Iterator<T> iterator) throws IOException {

    }

    /**
     * Interface for internal Field Entries
     */
    private interface FieldEntry extends Allocator {
        void write(DataOutput out) throws IOException;
    }

    /**
     * The simplest possible field, which consist of a single token without any value
     */
    private class TokenField implements FieldEntry {

        private int header;
        private String name;

        /**
         * Create a field for the type flag and
         * @param typeFlag The field type flag
         */
        public TokenField(int typeFlag) {
            this.header = typeFlag;
        }

        /**
         * Writes the field content to the data output
         * @param out The Data output to write to
         * @throws IOException If data writer raises one
         */
        public void write(DataOutput out) throws IOException {
            out.writeShort(header);
            if (name == null) return;
            out.writeUTF(name);
        }

        /**
         * Specifies a name for the field
         * @param name The name to give the field
         */
        @Override
        public void as(String name) {
            this.header |= fieldIndexMap.computeIfAbsent(name, n -> {
                if (nextFieldIndex >= (1 << 12)) {
                    throw new IllegalStateException("Max unique field/type name index count of 4096 exceeded.");
                }
                this.name = n;
                return nextFieldIndex++;
            }) << 4;
        }
    }

    /**
     * Field containing a value writer
     */
    private class DataOutputField extends TokenField {

        private final IOConsumer<DataOutput> valueWriter;

        /**
         * Create a field for the type flag and
         *
         * @param typeFlag The field type flag
         */
        public DataOutputField(int typeFlag, IOConsumer<DataOutput> valueWriter) {
            super(typeFlag);
            this.valueWriter = valueWriter;
        }

        /**
         * Overrides the write function to also output the value
         * @param out The Data output to write to
         * @throws IOException If thrown by DataOutput
         */
        @Override
        public void write(DataOutput out) throws IOException {
            super.write(out);
            this.valueWriter.accept(out);
        }
    }

    /**
     * Class used to queue object writing
     */
    /*
    private static class ObjectEntry {
        private final IOConsumer<DataOutput> objectHandler;
        private final String type;

        public ObjectEntry(Class<?> cls, IOConsumer<DataOutput> objectHandler) {
            this.type = cls.getName();
            this.objectHandler = objectHandler;
        }

        public void write(DataOutput out) throws IOException {
            this.objectHandler.accept(out);
        }
    }*/
}

