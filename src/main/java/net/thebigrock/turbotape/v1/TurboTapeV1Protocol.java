package net.thebigrock.turbotape.v1;

/**
 * Class containing common functionality for the FieldStreamProtocol V1
 */
public class TurboTapeV1Protocol {
    // The header found at the very beginning of the stream
    static final String PROTOCOL_HEADER = "FSP1";

    // The type flags
    static final int TYPE_FLAG_BOOLEAN_FALSE = 0x00;
    static final int TYPE_FLAG_BOOLEAN_TRUE = 0x01;
    static final int TYPE_FLAG_INTEGER_32 = 0x02;
    static final int TYPE_FLAG_INTEGER_64 = 0x03;
    static final int TYPE_FLAG_FLOAT_32 = 0x04;
    static final int TYPE_FLAG_FLOAT_64 = 0x05;
    static final int TYPE_FLAG_UTF_STRING = 0x06;
    static final int TYPE_FLAG_REF_OBJECT = 0x08;
    static final int TYPE_FLAG_REF_ARRAY = 0x09;

    // Define the bit size and mask
    static final int TYPE_FLAG_SIZE = 4;
    static final int TYPE_FLAG_MASK = (1 << TYPE_FLAG_SIZE) - 1;





}
