package net.thebigrock.turbotape.v1.util;

/**
 * Formats the content of a byte stream as a hex view
 */
public class HexViewFormatter {

    public static String format(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int ix = 0; ix < data.length; ix += 16) {
            builder.append(String.format("%04X: ", ix));

            for (int j = 0; j < 16; j++) {
                int pos = ix + j;
                if (j == 8) builder.append(" ");
                if (pos < data.length) {
                    builder.append(String.format("%02X ", data[pos]));
                }
                else {
                    builder.append("   ");
                }
            }

            builder.append(" ");

            for (int j = 0; j < 16; j++) {
                int pos = ix + j;
                if (pos < data.length) {
                    byte d = data[pos];
                    char c = d >= 32 && d < 127 ? (char)d : '.';
                    builder.append(String.format("%s", c));
                }
                else {
                    builder.append("   ");
                }
            }

            builder.append("\n");
        }
        return builder.toString();
    }

}
