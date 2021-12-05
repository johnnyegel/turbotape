package net.thebigrock.turbotape.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Resolves names for indexes.
 */
public class IndexResolver {
    private final Map<Integer, String> _indexMap = new HashMap<>();

    /**
     * Resolves the name associated with the index.
     *
     * If the index has not been seen before, the provided reader function is called
     * to read the name from the "tape". If the index is 0, null will be returned.
     *
     * @param index The index to resolve
     * @param readName Function that reads the name from the "tape" if not known
     * @return The resolved index name
     * @throws IOException If the read name function throws this
     */
    public Optional<String> resolve(int index, IOFunction<Integer, String> readName) throws IOException {
        // Null index results in an empty result
        if (index == 0) return Optional.empty();

        // Just return existing index if it exists
        if (_indexMap.containsKey(index)) return Optional.of(_indexMap.get(index));

        // If not, resolve the name
        String name = readName.apply(index);
        _indexMap.put(index, name);
        return Optional.of(name);
    }
}
