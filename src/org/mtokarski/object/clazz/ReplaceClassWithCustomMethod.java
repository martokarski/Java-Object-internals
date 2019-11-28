package org.mtokarski.object.clazz;

public class ReplaceClassWithCustomMethod {

    public static void main(String[] args) {
        Foo foo = new Foo();
        foo.customMethod();
        System.out.println();

        long objectPointer = ClassUtil.getKlassPointer(new Object());
        ClassUtil.putKlassPointer(foo, objectPointer);

        System.out.println("After replacing Foo to Object");
        foo.customMethod();
    }

    static class Foo {
        void customMethod() {
            System.out.println("Custom method invoked");
        }
    }
}
