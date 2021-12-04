package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.FieldWriter;
import net.thebigrock.turbotape.ObjectWriteHandlerProviderBuilder;
import net.thebigrock.turbotape.Serializer;
import net.thebigrock.turbotape.v1.util.HexViewFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

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
            writer.write(obj.b).as("bbbb");
            writer.write(obj.i).as("iii");
            writer.write(obj.l).as("llll");
            writer.write(obj.f).as("fff");
            writer.write(obj.d).as("ddd");
            writer.write(obj.str).as("str");
        }
    }

    @Test
    public void test_simple_serialization() throws IOException {

        // :: Arrange

        Serializer serializer = new TurboTapeV1Serializer(ObjectWriteHandlerProviderBuilder.create()
                .add("c1", TestClass1.class, TestClass1::serialize)
                .build());

        TestClass1 test1 = new TestClass1(true, 3, 100, 0.1f, 0.2d, "yoyoyo");
        TestClass1 test2 = new TestClass1(false, 123443, Long.MAX_VALUE, Float.MAX_VALUE, 0.2d, "yoy111");
        TestClass1 test3 = new TestClass1(true, -125153, Long.MIN_VALUE, -1.0f, Double.MIN_VALUE, "yoy2222");

        // :: Act

        byte[] bytes1 = serializer.serialize(TestClass1.class, test1);
        byte[] bytes2 = serializer.serialize(TestClass1.class, test2);
        byte[] bytes3 = serializer.serialize(TestClass1.class, test3);

        // :: Assert

        assertThat(bytes1.length).isEqualTo(86);
        assertThat(bytes2.length).isEqualTo(86);
        assertThat(bytes3.length).isEqualTo(87);

        System.out.println(HexViewFormatter.format(bytes3));

    }

}
