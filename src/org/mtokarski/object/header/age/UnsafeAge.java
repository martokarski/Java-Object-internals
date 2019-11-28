package org.mtokarski.object.header.age;

/**
 * Default gc algorithms tends to promote object in this example after age of 6.
 * Setting -XX:MaxTenuringThreshold=15 and -XX:+UseSerialGC should show age update up to limit.
 */
public class UnsafeAge {

    public static void main(String... args) throws InterruptedException {
        AgeUtil.registerListener();
        Object testObject = new Object();

        Thread thread = new Thread(() -> AgeUtil.printAgeIncrease(testObject));
        thread.start();
        Thread.sleep(400);
        thread.interrupt();
    }
}
