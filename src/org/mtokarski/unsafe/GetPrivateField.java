package org.mtokarski.unsafe;

import java.lang.reflect.Field;

public class GetPrivateField {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        final WithPrivateField withPrivateField = new WithPrivateField();

//        withPrivateField.privateNumber;
        System.out.println("Private field isn't accessible outside defining class");
        System.out.println();

        final Field privateNumberField = WithPrivateField.class.getDeclaredField("privateNumber");

        double valueUnsafe = getValueUnsafe(withPrivateField, privateNumberField);
        System.out.println("Private field read with Unsafe " + valueUnsafe);
        System.out.println();

        double value = getValue(withPrivateField, privateNumberField);
        System.out.println("Private field read with reflection " + value);
    }

    static double getValue(Object o, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.getInt(o);
    }

    static double getValueUnsafe(Object o, Field field) {
        final long offset = UnsafeUtil.unsafe.objectFieldOffset(field);
        return UnsafeUtil.unsafe.getInt(o, offset);
    }
}
