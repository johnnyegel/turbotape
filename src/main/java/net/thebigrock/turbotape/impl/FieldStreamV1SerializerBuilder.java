package net.thebigrock.turbotape.impl;

import net.thebigrock.turbotape.AbstractSerializerBuilder;
import net.thebigrock.turbotape.Serializer;

/**
 * Builds a FieldStream V1 Serializer
 */
public class FieldStreamV1SerializerBuilder extends AbstractSerializerBuilder {

    @Override
    public Serializer build() {
        return new FieldStreamProtocolV1Writer(classSerializerMap);
    }

}
