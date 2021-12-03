package net.thebigrock.turbotape.util;

import java.io.IOException;

/**
 * Consumer lambda interface that supports IOExceptions
 * @param <T> The type the consumer accepts
 */
public interface IOConsumer<T> {

    /**
     * Accept a consumer
     * @param t The consumer
     * @throws IOException IOException throw by consumer
     */
    void accept(T t) throws IOException;
}
