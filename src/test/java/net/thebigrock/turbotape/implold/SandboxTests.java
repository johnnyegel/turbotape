package net.thebigrock.turbotape.implold;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SandboxTests {
    @Test
    void test_ObjectWriterStringSeparation() throws IOException {
        final String test1 = "This is the first String";
        final String test2 = "Next String";
        final String test3 = "Then the last\n String in total";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);

        out.writeUTF(test1);
        out.writeUTF(test2);
        out.writeUTF(test3);
        out.close();

        byte[] data = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(inputStream);

        String str1 = in.readUTF();
        String str2 = in.readUTF();
        String str3 = in.readUTF();

        assertEquals(test1, str1);
        assertEquals(test2, str2);
        assertEquals(test3, str3);

    }
}
