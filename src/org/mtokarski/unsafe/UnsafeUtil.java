package org.mtokarski.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtil {
    public static final Unsafe unsafe;

    static {
        Unsafe init = null;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            init = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        unsafe = init;
    }

    private UnsafeUtil() {}
}
