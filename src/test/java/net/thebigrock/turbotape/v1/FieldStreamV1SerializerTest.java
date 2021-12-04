package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.FieldWriter;
import net.thebigrock.turbotape.Serializer;
import net.thebigrock.turbotape.v1.util.HexViewFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FieldStreamV1SerializerTest {

    public static class TestClass1 {
        private final boolean b;
        private final int i;
        private final long l;
        private final float f;
        private final double d;
        private final String str;

        TestClass1(boolean b, int i, long l, float f, double d, String str) {
            this.b = b;
            this.i = i;
            this.l = l;
            this.f = f;
            this.d = d;
            this.str = str;
        }

        public static void serialize(FieldWriter writer, TestClass1 obj) {
            writer.write(obj.b).as("bool");
            writer.write(obj.i).as("int");
            writer.write(obj.l).as("long");
            writer.write(obj.f).as("float");
            writer.write(obj.d).as("double");
            writer.write(obj.str).as("str");
        }
    }

    @Test
    public void test_simple_serialization() throws IOException {

        // :: Arrange
        FieldStreamV1SerializerBuilder serBuilder = new FieldStreamV1SerializerBuilder();
        serBuilder.add("c1", TestClass1.class, TestClass1::serialize);
        Serializer serializer = serBuilder.build();

        TestClass1 test = new TestClass1(true, 3, 100, 0.1f, 0.2d, "yoyoyo");

        // :: Act

        byte[] bytes = serializer.serialize(test);

        // :: Assert

        System.out.println(HexViewFormatter.format(bytes));


    }

}
