package org.mtokarski.unsafe;

import org.mtokarski.unsafe.UnsafeUtil;

import java.nio.ByteBuffer;

public class NativeMemory {

    public static void main(String[] args) {
        final CustomByteBuffer customByteBuffer = new CustomByteBuffer(13);
        customByteBuffer.put((byte) 2);
        customByteBuffer.putInt(-24);
        customByteBuffer.putDouble(10e-1);
        customByteBuffer.rewind();

        System.out.println("CustomByteBuffer contains:");
        System.out.println(customByteBuffer.get());
        System.out.println(customByteBuffer.getInt());
        System.out.println(customByteBuffer.getDouble());
        System.out.println();

        //this method returns DirectByteBuffer - package private class using unsafe to store data
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(13);
        byteBuffer.put((byte) 2);
        byteBuffer.putInt(-24);
        byteBuffer.putDouble(10e-1);
        byteBuffer.rewind();

        System.out.println("DirectByteBuffer contains:");
        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getDouble());
    }

    private static class CustomByteBuffer {

        private final long size;
        private final long address;

        private long counter;

        CustomByteBuffer(long size) {
            this.size = size;
            address = UnsafeUtil.unsafe.allocateMemory(size);
        }

        byte get() {
            checkOverflow(1);
            return UnsafeUtil.unsafe.getByte(address + counter++);
        }

        int getInt() {
            checkOverflow(4);
            final int value = UnsafeUtil.unsafe.getInt(address + counter);
            counter += 4;
            return value;
        }

        double getDouble() {
            checkOverflow(8);
            final double value = UnsafeUtil.unsafe.getDouble(address + counter);
            counter += 8;
            return value;
        }

        void put(byte val) {
            checkOverflow(1);
            UnsafeUtil.unsafe.putByte(address + counter++, val);
        }

        void putInt(int val) {
            checkOverflow(4);
            UnsafeUtil.unsafe.putInt(address + counter, val);
            counter += 4;
        }

        void putDouble(double val) {
            checkOverflow(8);
            UnsafeUtil.unsafe.putDouble(address + counter, val);
            counter += 8;
        }

        void rewind() {
            counter = 0;
        }

        private void checkOverflow(int increment) {
            if (counter + increment > size) {
                throw new IndexOutOfBoundsException();
            }
        }
    }
}
