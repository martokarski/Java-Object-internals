package org.mtokarski.unsafe;

import org.mtokarski.unsafe.UnsafeUtil;

import java.io.IOException;

/**
 *
 */
public class ThrowCheckedException {

    public static void main(String[] args) {
        try {
            throwDeclaredException();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            throwCheckedUnsafe();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            throwCheckedException();
        }
    }

    private static void throwDeclaredException() throws IOException {
        throw new IOException("Checked exceptions have to be declared or handled");
    }

    private static void throwCheckedUnsafe() {
        UnsafeUtil.unsafe.throwException(new IOException("Thrown checked exception without declaration with unsafe"));

    }

    private static void throwCheckedException() {
        throwChecked(new IOException("Thrown checked exception without declaration with generics"));
    }

    //generics are checked during compilation - in bytecode all 'T' will be replaced by Throwable
    private static <T extends Throwable> void throwChecked(Throwable e) throws T {
        //Compiler cannot determine actual class to cast, so it raises warning.
        //Compiler will replace it with cast to Throwable. In this case it will be omitted, because argument already has proper type.
        throw (T) e;
    }
}
