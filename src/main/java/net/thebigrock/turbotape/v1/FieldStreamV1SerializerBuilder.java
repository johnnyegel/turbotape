package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.AbstractSerializerBuilder;
import net.thebigrock.turbotape.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * Builds a serializer for the FieldStream V1 protocol
 */
public class FieldStreamV1SerializerBuilder extends AbstractSerializerBuilder {
    @Override
    public Serializer build() {
        return new SerializerImpl(Collections.unmodifiableMap(classSerializerMap));
    }

    /**
     * Implements the serializer for the V1 protocol
     */
    private static class SerializerImpl implements Serializer {
        private final Map<Class<?>, ClassAlias> _classSerializeMap;

        private SerializerImpl(Map<Class<?>, ClassAlias> classSerializeMap) {
            _classSerializeMap = classSerializeMap;
        }

        @Override
        public <T> void serialize(T object, OutputStream outputStream) throws IOException {
            FieldStreamV1ObjectWriter objectWriter = new FieldStreamV1ObjectWriter(_classSerializeMap, object);
            DataOutputStream dataWriter = new DataOutputStream(outputStream);
            dataWriter.write(FieldStreamProtocolV1.PROTOCOL_HEADER.getBytes(StandardCharsets.UTF_8));
            objectWriter.write(new DataOutputStream(outputStream));
        }
    }

}
