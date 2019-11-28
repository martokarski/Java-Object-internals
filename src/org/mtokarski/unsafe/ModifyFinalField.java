package org.mtokarski.unsafe;

import org.mtokarski.unsafe.UnsafeUtil;

import java.lang.reflect.Field;

public class ModifyFinalField {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        final ClassWithFinalField classWithFinalField = new ClassWithFinalField();

        System.out.println("Final field with value " + classWithFinalField.number);
//        classWithFinalField.number = 121;
        System.out.println("It cannot be modified in Java code");
        System.out.println();

        final Field numberField = ClassWithFinalField.class.getDeclaredField("number");

        setValueUnsafe(classWithFinalField, numberField);
        System.out.println("Final field changed with Unsafe to " + classWithFinalField.number);
        System.out.println();

        setValue(classWithFinalField, numberField);
        System.out.println("Final field changed with reflection to " + classWithFinalField.number);
    }

    private static void setValue(Object o, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        field.setInt(o, 1);
    }

    private static void setValueUnsafe(Object o, Field field) {
        final long offset = UnsafeUtil.unsafe.objectFieldOffset(field);
        UnsafeUtil.unsafe.putInt(o, offset, 10);
    }

    private static class ClassWithFinalField {
        final int number;

        ClassWithFinalField() {
            number = 5;
        }
    }
}
