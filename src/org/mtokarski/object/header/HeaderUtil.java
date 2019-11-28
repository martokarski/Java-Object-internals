package org.mtokarski.object.header;

import org.mtokarski.unsafe.UnsafeUtil;

public final class HeaderUtil {

    private static final long LOCK_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011L;
    private static final long BIASED_LOCK_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000100L;
    private static final long AGE_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01111000L;
    private static final long HASH_MASK = 0b00000000_00000000_00000000_01111111_11111111_11111111_11111111_00000000L;

    private static final long JAVA_PTR_MASK = 0b11111111_11111111_11111111_11111111_11111111_11111111_11111100_00000000L;
    private static final long PTR = 0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111100L;

    private static final int AGE_SHIFT = 3;
    private static final int HASH_SHIFT = AGE_SHIFT + 5;

    private static final int UNLOCK = 1;
    private static final int LIGHT_LOCK = 0;
    private static final int HEAVY_LOCK = 2;
    private static final int BIASED_LOCK = 4;

    private HeaderUtil() {
    }

    public static long getObjectHeader(Object o) {
        return UnsafeUtil.unsafe.getLong(o, 0L);
    }

    public static long getDisplacedHeader(Object o) {
        long lockPointer = getObjectHeader(o);
        long lockBits = LOCK_MASK & lockPointer;
        if (lockBits != LIGHT_LOCK && lockBits != HEAVY_LOCK) {
            throw new IllegalStateException("Header isn't displaced");
        }

        return extractDisplacedHeader(lockPointer);
    }

    private static long extractDisplacedHeader(long lockPointer) {
        lockPointer &= PTR;
        return UnsafeUtil.unsafe.getLong(lockPointer);
    }

    public static long setUnlocked(Object o) {
        long originalHeader = UnsafeUtil.unsafe.getLong(o, 0L);
        long header = ((originalHeader >> 2) << 2) + 1;
        UnsafeUtil.unsafe.putLong(o, 0L, header);

        return originalHeader;
    }

    public static void setHeader(Object o, long header) {
        UnsafeUtil.unsafe.putLong(o, 0L, header);
    }

    public static long getObjectAge(Object o) {
        long header = getObjectHeader(o);
        final LockState lockState = extractLockState(header);

        if (lockState == LockState.LOCKED || lockState == LockState.MONITOR) {
            header = extractDisplacedHeader(header);
        }

        return extractAge(header);
    }

    public static long getObjectHash(Object o) {
        long header = getObjectHeader(o);
        final LockState lockState = extractLockState(header);

        if (lockState == LockState.READY_TO_BIAS || lockState == LockState.BIASED) {
            return 0;
        }

        if (lockState == LockState.LOCKED || lockState == LockState.MONITOR) {
            header = extractDisplacedHeader(header);
        }

        return extractHash(header);
    }

    public static LockState extractLockState(Object o) {
        long objectHeader = getObjectHeader(o);
        return extractLockState(objectHeader);
    }

    private static LockState extractLockState(long header) {

        int lockBits = extractLockBits(header);

        switch (lockBits) {
            case UNLOCK:
                if (isBiasedLock(header)) {
                    long javaPtr = (header & JAVA_PTR_MASK);
                    if (javaPtr == 0) {
                        return LockState.READY_TO_BIAS;
                    } else {
                        return LockState.BIASED;
                    }
                }
                return LockState.UNLOCKED;
            case LIGHT_LOCK:
                return LockState.LOCKED;
            case HEAVY_LOCK:
                return LockState.MONITOR;
            default:
                throw new IllegalStateException("Cannot parse header properly");
        }
    }

    private static int extractLockBits(long header) {
        return (int) (header & LOCK_MASK);
    }

    private static boolean isBiasedLock(long header) {
        return (header & BIASED_LOCK_MASK) == BIASED_LOCK;
    }

    private static int extractAge(long header) {
        return (int) ((header & AGE_MASK) >> AGE_SHIFT);
    }

    private static int extractHash(long header) {
        return (int) ((header & HASH_MASK) >> HASH_SHIFT);
    }

    public static void saveHash(Object o, int hash) {
        UnsafeUtil.unsafe.putInt(o, 1L, hash);
    }

    public static void saveAge(Object o, int age) {
        byte agePart = (byte) ((age & 15) << AGE_SHIFT);
        byte firstByte = UnsafeUtil.unsafe.getByte(o, 0L);
        byte onlyLockInfo = (byte) (firstByte & (LOCK_MASK | BIASED_LOCK_MASK));
        UnsafeUtil.unsafe.putInt(o, 0L, agePart + onlyLockInfo);
    }

    public enum LockState {
        LOCKED, UNLOCKED, MONITOR, READY_TO_BIAS, BIASED
    }
}
