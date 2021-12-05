package net.thebigrock.turbotape.v1;

import net.thebigrock.turbotape.FieldReader;

public class TurboTapeV1ObjectReader {


    private class FieldReaderImpl implements FieldReader {

        @Override
        public FieldReader at(int index) {
            return null;
        }

        @Override
        public FieldReader at() {
            return null;
        }

        @Override
        public FieldReader as(String name) {
            return null;
        }

        @Override
        public int readBool() {
            return 0;
        }

        @Override
        public int readInt() {
            return 0;
        }

        @Override
        public long readLong() {
            return 0;
        }

        @Override
        public float readFloat() {
            return 0;
        }

        @Override
        public double readDouble() {
            return 0;
        }

        @Override
        public String readString() {
            return null;
        }

        @Override
        public <T> T readObject(Class<T> cls) {
            return null;
        }

        @Override
        public <T> Iterable<T> readIterable(Class<T> cls) {
            return null;
        }
    }
}
