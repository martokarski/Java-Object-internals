package org.mtokarski.object.fields;

import org.mtokarski.unsafe.UnsafeUtil;

import java.lang.reflect.Field;

public class FieldAccess {

    public static void main(String[] args) throws NoSuchFieldException {
        ClassWithField classWithField = new ClassWithField();

        Field someField = ClassWithField.class.getDeclaredField("someField");
        long fieldOffset = UnsafeUtil.unsafe.objectFieldOffset(someField);
        System.out.println("Offset of the field is " + fieldOffset);
        System.out.println();

        int someFieldValue = UnsafeUtil.unsafe.getInt(classWithField, fieldOffset);
        System.out.println("Value of the field is " + someFieldValue);
        System.out.println();

        UnsafeUtil.unsafe.putInt(classWithField, fieldOffset, 3126);

        someFieldValue = UnsafeUtil.unsafe.getInt(classWithField, fieldOffset);
        System.out.println("After change, value of the field is " + someFieldValue);
    }

    private static class ClassWithField {
        private int someField = 5;
    }
}
