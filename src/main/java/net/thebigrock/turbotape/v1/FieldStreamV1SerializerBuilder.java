package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.AbstractSerializerBuilder;
import net.thebigrock.turbotape.ObjectWriter;
import net.thebigrock.turbotape.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        private final Map<Class<?>, ObjectWriter> _classSerializeMap;

        private SerializerImpl(Map<Class<?>, ObjectWriter> classSerializeMap) {
            _classSerializeMap = classSerializeMap;
        }

        @Override
        public <T> void serialize(T object, OutputStream outputStream) throws IOException {
            FieldStreamV1ObjectWriter objectWriter = new FieldStreamV1ObjectWriter(_classSerializeMap, object);
            objectWriter.write(new DataOutputStream(outputStream));
        }
    }

}
