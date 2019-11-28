package org.mtokarski.object.header.lock;

import org.mtokarski.object.header.HeaderUtil;

/**
 * Setting -XX:BiasedLockingStartupDelay=0 will enable biased locking
 */
public class BiasedLock {

    public static void main(String... args) throws InterruptedException {
        Object o = new Object();
        System.out.println("Initially object is: " + HeaderUtil.extractLockState(o));

        synchronized (o) {
            System.out.println("When used to lock, object is: " + HeaderUtil.extractLockState(o));
        }

        System.out.println("After synchronisation object is: " + HeaderUtil.extractLockState(o));

        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println("When synchronised in a different thread object is: " + HeaderUtil.extractLockState(o));
            }
        });
        thread.start();
        thread.join();
        System.out.println("After synchronisation object is: " + HeaderUtil.extractLockState(o));
    }
}
