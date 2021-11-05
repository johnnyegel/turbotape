package net.thebigrock.turbotape.impl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Type hash provider that use SHA2-256 to generate hashes for the type strings.
 * <p>
 * It generates a 256 bit hash for the given typeString, and then creates a 31-bit integer
 * by using the 4 first bytes from the generated hash and applying mask 0x7FFFFFFF.
 * <p>
 * This is not the fastest way to generate suitable hashes, but it's simple to do in
 * most programming languages. This makes it simple to implement de-serialization in
 * other languages than Java. Finally, hashes are only generated when the serializer
 * is built. So it does not matter that the performance is not _peak_ at this.
 */
public class Sha2TypeHashProvider implements TypeHashProvider {


    private final MessageDigest _digest;

    /**
     * Creates a new Hash provider.
     * @throws NoSuchAlgorithmException If there is no SHA-256 algorithm available
     */
    public Sha2TypeHashProvider() throws NoSuchAlgorithmException {
        _digest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public int getHash(String typeString) {
        byte[] hash = _digest.digest(typeString.getBytes(StandardCharsets.UTF_8));
        return ByteBuffer.wrap(hash).getInt() & HASH_BITMASK;
    }
}
