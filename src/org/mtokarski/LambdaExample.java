package org.mtokarski;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LambdaExample {

    public static void main(String[] args) throws Throwable {
        LambdaExample lambdaExample = new LambdaExample();
        Consumer<String> consumer = string -> lambdaExample.consume(string);
        consumer.accept("Hello");
        System.out.println(consumer.getClass().isSynthetic());
        Consumer check = consumer;
//        check.accept(new Integer(1));
        for (Method method : consumer.getClass().getMethods()) {
            System.out.println(method);
        }
        System.out.println();
        consumer = lambdaExample::consume;

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Consumer<String> reflectiveConsumer = getConsumer(lambdaExample);

        reflectiveConsumer.accept("From consumer created in code");
        System.out.println(reflectiveConsumer.getClass());
        for (Type genericInterface : reflectiveConsumer.getClass().getGenericInterfaces()) {
            System.out.println(genericInterface);
        }
        System.out.println();
        for (Method method : reflectiveConsumer.getClass().getMethods()) {
            System.out.println(method);
        }

        Comparator<String> val = String::compareTo;
        Comparator<String> o = (Comparator<String>) LambdaMetafactory.metafactory(
                lookup,
                "compare",
                MethodType.methodType(Comparator.class),
                MethodType.methodType(int.class, Object.class, Object.class),
                lookup.findVirtual(String.class, "compareTo", MethodType.methodType(int.class, Object.class)),
                MethodType.methodType(int.class, String.class, Object.class)
        ).getTarget().invokeExact();

        BiConsumer<A, Object> a = LambdaExample::consume;

        System.out.println(val.compare("", "1"));
        System.out.println(o.compare("", "1"));
    }

    static Consumer<String> getConsumer(LambdaExample lambdaExample) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Object o = LambdaMetafactory.metafactory(
                lookup,
                "accept",
                MethodType.methodType(Consumer.class, LambdaExample.class),
                MethodType.methodType(void.class, Object.class),
                lookup.findVirtual(LambdaExample.class, "consume", MethodType.methodType(void.class, Object.class)),
                MethodType.methodType(void.class, String.class)
        ).getTarget().invoke(lambdaExample);
        return (Consumer<String>) o;
    }

    <T> void consume(T value) {
        LambdaExample lambdaExample = this;
        Class<? extends LambdaExample> aClass = lambdaExample.getClass();
        Consumer<String> consumer = lambdaExample::consume;
        System.out.println(value);
        hashCode()
    }

    static class A extends LambdaExample {
    }
}
