package org.mtokarski.object.header.age;

import org.mtokarski.object.header.HeaderUtil;

/**
 * It is possible to change object's age. If GC algorithm uses its value, object can be stored in young gen indefinitely.
 */
public class AlwaysYoungAge {

    public static void main(String... args) throws InterruptedException {
        AgeUtil.registerListener();

        Object testObject = new Object();
        Thread thread = new Thread(() -> AgeUtil.printAgeIncrease(testObject, age -> {
            if (age >= 5) {
                HeaderUtil.saveAge(testObject, 1);
                System.out.println("Age reset - object is 'reborn'");
            }
            return true;
        }));
        thread.start();
        Thread.sleep(1_000);
        thread.interrupt();
    }
}
