package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.FieldWriter;
import net.thebigrock.turbotape.ObjectWriteHandler;
import net.thebigrock.turbotape.ObjectWriteHandlerProvider;
import net.thebigrock.turbotape.util.IOConsumer;
import net.thebigrock.turbotape.util.IndexAllocator;

import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class TurboTapeV1ObjectWriter<T> extends TurboTapeV1Protocol {
    private final Queue<TurboTapeV1ObjectWriter<?>> _objectFifo = new LinkedList<>();
    private final Context _context;
    private final T _object;

    /**
     * Creates the initial object writer, initializing a context from thw writer class map
     * @param writerProvider The writer provider to use to resolve object writer
     * @param object The initial object to serialize
     */
    TurboTapeV1ObjectWriter(ObjectWriteHandlerProvider writerProvider, T object) {
        this(new Context(writerProvider), object);
    }

    /**
     * Creates a writer for a given object
     * @param context The writer context
     * @param object The object to write
     */
    private TurboTapeV1ObjectWriter(Context context, T object) {
        this._context = context;
        this._object = object;
    }

    /**
     * Writes the fields to the Data Output
     * @param out The Data Output to write to
     * @throws IOException If an IO exception occurs
     */
    void write(DataOutput out) throws IOException {
        // Get the class writer
        String objectAlias = _context._writerProvider.getAlias(_object.getClass());

        // Allocate object type index
        IndexAllocator.Index index = _context._objectAllocator.allocate(objectAlias);

        // Write the index and potentially the typename
        out.writeShort(index.index());
        index.write(out::writeUTF);

        // Execute the class writer to retrieve the object fields
        FieldWriterImpl fieldWriter = new FieldWriterImpl();
        ObjectWriteHandler<T> objectWriter = _context._writerProvider.getWriteHandler(_object.getClass());
        objectWriter.process(fieldWriter, _object);

        // Write the data
        fieldWriter.writeData(out);

        // Then iterate the sub-objects, and write
        while (!_objectFifo.isEmpty()) {
            TurboTapeV1ObjectWriter<?> subObjectWriter = _objectFifo.remove();
            subObjectWriter.write(out);
        }
    }


    /**
     * Context object containing the class
     */
    private static class Context {
        private final ObjectWriteHandlerProvider _writerProvider;
        private final IndexAllocator _fieldAllocator = new IndexAllocator(1 << (16 - TYPE_FLAG_SIZE));
        private final IndexAllocator _objectAllocator = new IndexAllocator(1 << 16);

        private Context(ObjectWriteHandlerProvider writerProvider) {
            this._writerProvider = writerProvider;
        }
    }

    /**
     * Writer which is passed to serialization methods
     */
    private class FieldWriterImpl implements FieldWriter {
        private final Queue<FieldEntry> _fieldFifo = new LinkedList<>();

        @Override
        public Allocator write(boolean value) {
            return add(new FieldEntry(value ? TYPE_FLAG_BOOLEAN_TRUE : TYPE_FLAG_BOOLEAN_FALSE));
        }

        @Override
        public Allocator write(int value) {
            return add(new DataFieldEntry(TYPE_FLAG_INTEGER_32, out -> out.writeInt(value)));
        }

        @Override
        public Allocator write(long value) {
            return add(new DataFieldEntry(TYPE_FLAG_INTEGER_64, out -> out.writeLong(value)));
        }

        @Override
        public Allocator write(float value) {
            return add(new DataFieldEntry(TYPE_FLAG_FLOAT_32, out -> out.writeFloat(value)));
        }

        @Override
        public Allocator write(double value) {
            return add(new DataFieldEntry(TYPE_FLAG_FLOAT_64, out -> out.writeDouble(value)));
        }

        @Override
        public Allocator write(String value) {
            return add(new DataFieldEntry(TYPE_FLAG_UTF_STRING, out -> out.writeUTF(value)));
        }

        @Override
        public <T> Allocator write(T object) {
            Allocator objectAllocator = add(new FieldEntry(TYPE_FLAG_REF_OBJECT));
            _objectFifo.add(new TurboTapeV1ObjectWriter<T>(_context, object));
            return objectAllocator;
        }

        @Override
        public <T> Allocator write(Iterator<T> objects) {
            throw new UnsupportedOperationException("NOT IMPLEMENTED");
        }

        private Allocator add(FieldEntry entry) {
            _fieldFifo.add(entry);
            return entry;
        }

        private void writeData(DataOutput out) throws IOException {
            while (!_fieldFifo.isEmpty()) {
                FieldEntry field = _fieldFifo.remove();
                field.write(out);
            }
        }
    }

    /**
     * The simplest possible field, which consist of a single token without any value
     */
    private class FieldEntry implements FieldWriter.Allocator {

        private final int type;
        private IndexAllocator.Index index;

        /**
         * Create a field for the type flag and
         * @param typeFlag The field type flag
         */
        public FieldEntry(int typeFlag) {
            this.type = typeFlag;
            this.index = IndexAllocator.NULL_INDEX;
        }

        /**
         * Writes the field content to the data output
         * @param out The Data output to write to
         * @throws IOException If data writer raises one
         */
        public void write(DataOutput out) throws IOException {
            out.writeShort(index.index() << 4 | this.type);
            index.write(out::writeUTF);
        }

        /**
         * Specifies a name for the field
         * @param name The name to give the field
         */
        @Override
        public void as(String name) {
            index = _context._fieldAllocator.allocate(name);
        }
    }

    /**
     * Field containing a value writer
     */
    private class DataFieldEntry extends FieldEntry {

        private final IOConsumer<DataOutput> valueWriter;

        /**
         * Create a field for the type flag and
         *
         * @param typeFlag The field type flag
         */
        public DataFieldEntry(int typeFlag, IOConsumer<DataOutput> valueWriter) {
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

}
