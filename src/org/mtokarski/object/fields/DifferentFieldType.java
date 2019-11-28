package org.mtokarski.object.fields;

import org.mtokarski.unsafe.UnsafeUtil;

import java.lang.reflect.Field;

public class DifferentFieldType {

    public static void main(String[] args) throws NoSuchFieldException {
        ClassWithField classWithField = new ClassWithField();
        new Integer(1);

        Field someField = DifferentFieldType.ClassWithField.class.getDeclaredField("someField");
        long fieldOffset = UnsafeUtil.unsafe.objectFieldOffset(someField);
        System.out.println("Offset of the field is " + fieldOffset);
        System.out.println();

        long someFieldValue = UnsafeUtil.unsafe.getLong(classWithField, fieldOffset);
        System.out.println("Value of field is " + someFieldValue);
        System.out.println();

        int intValue = UnsafeUtil.unsafe.getInt(classWithField, fieldOffset);
        System.out.println("Integer value from field offset is " + intValue);
        System.out.println();

        double doubleValue = UnsafeUtil.unsafe.getDouble(classWithField, fieldOffset);
        System.out.println("Double value from field offset is " + doubleValue);
        System.out.println();

        //Uncommenting code below will cause JVM fatal error
//        Object objectValue = HeaderUtil.unsafe.getObject(classWithField, fieldOffset);
//        System.out.println("Object value from field offset is " + objectValue);
    }

    private static class ClassWithField {
        long someField = 13846978485156L;
    }
}
