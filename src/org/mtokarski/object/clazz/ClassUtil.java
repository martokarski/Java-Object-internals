package org.mtokarski.object.clazz;

import org.mtokarski.unsafe.UnsafeUtil;

public final class ClassUtil {
    public static long getKlassPointer(Object o) {
        return UnsafeUtil.unsafe.getLong(o, 8L);
    }

    public static void putKlassPointer(Object o, long klassPointer) {
        UnsafeUtil.unsafe.putLong(o, 8L, klassPointer);
    }

    private ClassUtil() {}
}
