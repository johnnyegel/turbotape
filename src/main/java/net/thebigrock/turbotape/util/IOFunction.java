package net.thebigrock.turbotape.util;

import java.io.IOException;
import java.util.function.Function;

/**
 * Function lambda interface that supports IOExceptions
 * @param <T> The type the consumer accepts
 */
public interface IOFunction<T, R> {
    /**
     * Applies the function
     * @param input The input
     * @return The result
     * @throws IOException if process raised an IOException
     */
    R apply(T input) throws IOException;
}
