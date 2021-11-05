package net.thebigrock.turbotape.impl;

/**
 * A Type Hash provider provides a Pseudo unique 31-bit Hash value for strings
 * intended to represent types. It also provides a default implementation for generating
 * hashes for Class'es using their SimpleName as the Type String.
 * <p>
 * The top-most bit (the sign bit) is used to indicate conflicts, and thus should be masked out.
 * <p>
 * Hashes this short can of course generate conflicts, however they are unlikely.
 * As the number of types handled by a (de)serializer is (should be) quite small,
 * this is unlikely to be a common problem. Also, the user also have the option of
 * using aliases to types instead of the class names, should class names cause conflicts.
 * <p>
 * Due to this fact, the top most bit is reserved for conflict indication, and
 * should not be set by Hash Providers.
 */
public interface TypeHashProvider {
    int HASH_BITMASK = 0x7FFFFFFF;

    /**
     * Provides a hash for the given type string
     * @param typeString The type string
     * @return The hash as an unsigned integer
     */
    int getHash(String typeString);

    /**
     * Provides a hash from a given class' {@link Class#getSimpleName()} method
     * @param cls The class to create hash for
     * @return The hash as an unsigned integer
     */
    default int getHash(Class<?> cls) {
        return getHash(cls.getSimpleName());
    }
}
