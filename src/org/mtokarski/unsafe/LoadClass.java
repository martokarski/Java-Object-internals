package org.mtokarski.unsafe;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

/**
 * To load class safely add -Dsafe=true to VM arguments
 */
public class LoadClass {

    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = "com.example.MessageClass";
        try {
            LoadClass.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + className + " not found");
            System.out.println();
        }

        byte[] classBytes = Files.readAllBytes(Paths.get("./resources/MessageClass.class"));
        Class<?> messageClass = defineClass(className, classBytes, LoadClass.class.getClassLoader());
        System.out.println(messageClass + " loaded!");
        System.out.println();

        Object message = messageClass.getConstructor().newInstance();
        System.out.println(message);
        Method setMessage = messageClass.getDeclaredMethod("setMessage", String.class);
        setMessage.invoke(message, "It works! Method was invoked.");
        System.out.println(message);
    }

    private static Class<?> defineClass(String name, byte[] bytes, ClassLoader classLoader) {
        if ("true".equals(System.getProperty("safe"))) {
            final InternalClassLoader internalClassLoader = new InternalClassLoader(classLoader);
            return internalClassLoader.defineClass(name, bytes, null);
        } else {
            System.out.println("Using unsafe to load class");
            System.out.println();
            return UnsafeUtil.unsafe.defineClass(name, bytes, 0, bytes.length, classLoader, null);
        }
    }

    private static class InternalClassLoader extends ClassLoader {

        InternalClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        public Class<?> defineClass(String name, byte[] bytes, ProtectionDomain protectionDomain) {
            return defineClass(name, bytes, 0, bytes.length, protectionDomain);
        }
    }
}
