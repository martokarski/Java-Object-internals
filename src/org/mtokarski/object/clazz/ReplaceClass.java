package org.mtokarski.object.clazz;

public class ReplaceClass {

    public static void main(String[] args) {
        Object o = new Object();
        printClassObject(Object.class, o);

        Foo foo = new Foo();
        printClassObject(Foo.class, foo);

        Bar bar = new Bar();
        printClassObject(Bar.class, bar);

        long objectKlass = ClassUtil.getKlassPointer(o);
        long fooKlass = ClassUtil.getKlassPointer(foo);
        long barKlass = ClassUtil.getKlassPointer(bar);

        System.out.println("Replace Object with Foo");
        ClassUtil.putKlassPointer(o, fooKlass);
        printClassObject(Object.class, o);

        System.out.println("Replace Foo with Bar");
        ClassUtil.putKlassPointer(foo, barKlass);
        printClassObject(Foo.class, foo);

        System.out.println("Replace Bar with Object");
        ClassUtil.putKlassPointer(bar, objectKlass);
        printClassObject(Bar.class, bar);
    }

    private static <T> void printClassObject(Class<T> clazz, T t) {
        System.out.println(clazz.getSimpleName() + " object prints: " + t);
        System.out.println(clazz.getSimpleName() + " class is: " + t.getClass().getSimpleName());
        System.out.println();
    }

    static class Foo {
        @Override
        public String toString() {
            return "Foo class";
        }
    }

    static class Bar {
        @Override
        public String toString() {
            return "Bar class";
        }
    }
}
