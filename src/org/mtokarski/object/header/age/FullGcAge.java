package org.mtokarski.object.header.age;

/**
 * Default gc algorithms tends to promote object in this example after age of 6.
 * Setting -XX:MaxTenuringThreshold=15 and -XX:+UseSerialGC should show age update up to limit.
 */
public class FullGcAge {

    public static void main(String... args) throws InterruptedException {
        AgeUtil.registerListener();

        Object testObject = new Object();
        Thread thread = new Thread(() -> AgeUtil.printAgeIncrease(testObject, age -> {
            if (age >= 7) {
                System.gc();
                System.out.println("After Full GC, object will be moved to old gen.");
            }
            return true;
        }));
        thread.start();
        Thread.sleep(400);
        thread.interrupt();
    }
}
