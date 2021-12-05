package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.ObjectWriteHandlerProvider;
import net.thebigrock.turbotape.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * TurboTape serializer instance
 */
public class TurboTapeV1Serializer implements Serializer {

    private final ObjectWriteHandlerProvider _writerProvider;

    public TurboTapeV1Serializer(ObjectWriteHandlerProvider writerProvider) {
        _writerProvider = writerProvider;
    }

    @Override
    public <T> void serialize(T object, OutputStream outputStream) throws IOException {
        TurboTapeV1ObjectWriter<T> objectWriter = new TurboTapeV1ObjectWriter<>(_writerProvider, object);
        DataOutputStream dataWriter = new DataOutputStream(outputStream);
        dataWriter.write(TurboTapeV1Protocol.PROTOCOL_HEADER.getBytes(StandardCharsets.UTF_8));
        objectWriter.write(new DataOutputStream(outputStream));
    }
}
