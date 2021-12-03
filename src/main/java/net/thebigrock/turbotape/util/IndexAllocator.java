package net.thebigrock.turbotape.util;

import net.thebigrock.turbotape.util.IOConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Allocates indices for given names, if the name is not already known.
 * If a known name is given, the existing index is re-used.
 */
public class IndexAllocator {
    /**
     * Represents a Null index
     */
    public static final Index NULL_INDEX = new NullIndex();

    // Internal variables
    private final Map<String, Integer> _indexMap = new HashMap<>();
    private final int limitValue;

    // State
    private int nextIndex = 1;

    /**
     * Create allocator that is limited to the given value
     *
     * Once reached, the allocate method will throw an exception if called with a new unknown name.
     *
     * @param limitValue The limit value
     */
    public IndexAllocator(int limitValue) {
        this.limitValue = limitValue;
    }

    /**
     * Allocate new or find existing index for the given name
     * @param name The name to allocate or look up
     * @return The index representing the name
     * @throws IndexOutOfBoundsException if entire index range has been exceeded
     */
    public Index allocate(String name) {
        // :? Does name index exist?
        if (_indexMap.containsKey(name)) {
            // -> Yes, then return Existing Index instance
            return new ExistingIndex(_indexMap.get(name));
        }
        // E-> No, make sure limit is not reached, then allocate new index
        if (nextIndex >= this.limitValue) {
            throw new IndexOutOfBoundsException("Index limit [" + limitValue + "] exceeded");
        }
        _indexMap.put(name, nextIndex);
        return new NewIndex(nextIndex++, name);
    }

    /**
     * Represents an allocated Index
     */
    public interface Index {

        /**
         * The index allocated
         * @return The index
         */
        int index();

        /**
         * Calls the consumer if the index was newly allocated.
         * If not the call is ignored.
         * @param consumer The consumer called with the string
         */
        void write(IOConsumer<String> consumer) throws IOException;
    }

    /**
     * The null index is used for entries that has no index
     */
    private static class NullIndex implements Index {
        @Override
        public int index() {
            return 0;
        }

        @Override
        public void write(IOConsumer<String> consumer) throws IOException {
            // NO-OP: Does nothing here
        }
    }

    /**
     * Existing index represents an existing index. It will not call the consumer on write().
     */
    private static class ExistingIndex implements Index {
        private int index;

        public ExistingIndex(int index) {
            this.index = index;
        }

        @Override
        public int index() {
            return this.index;
        }

        @Override
        public void write(IOConsumer<String> consumer) throws IOException {
            // NO-OP: Does nothing here
        }
    }

    /**
     * New Index calls consumer on write.
     */
    private static class NewIndex extends ExistingIndex {
        private int index;
        private String name;

        public NewIndex(int index, String name) {
            super(index);
            this.name = name;
        }

        @Override
        public void write(IOConsumer<String> consumer) throws IOException {
            consumer.accept(this.name);
        }
    }
}
