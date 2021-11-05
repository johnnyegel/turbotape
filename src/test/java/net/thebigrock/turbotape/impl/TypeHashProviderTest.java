package net.thebigrock.turbotape.impl;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TypeHashProviderTest {

    @Test
    void test_Sha2TypeHashProviderConsistent() throws NoSuchAlgorithmException {
        TypeHashProvider provider = new Sha2TypeHashProvider();
        
        int hash1a = provider.getHash("Map");
        int hash1b = provider.getHash(Map.class);

        int hash2a = provider.getHash("List");
        int hash2b = provider.getHash(List.class);
        int hash2c = provider.getHash("List.");

        assertEquals(hash1a, hash1b);
        assertEquals(hash2a, hash2b);
        assertNotEquals(hash1a, hash2a);
        assertNotEquals(hash2a, hash2c);
    }
}
