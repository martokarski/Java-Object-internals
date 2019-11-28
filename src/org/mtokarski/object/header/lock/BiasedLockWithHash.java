package org.mtokarski.object.header.lock;

import org.mtokarski.object.header.HeaderUtil;

/**
 * Setting -XX:BiasedLockingStartupDelay=0 will enable biased locking
 */
public class BiasedLockWithHash {

    public static void main(String... args) throws InterruptedException {
        Object o = new Object();

        System.out.println("Initially object is: " + HeaderUtil.extractLockState(o));
        o.hashCode();
        System.out.println("After calculating hash, object is: " + HeaderUtil.extractLockState(o));
        System.out.println();

        o = new Object();
        System.out.println("Next object initially is: " + HeaderUtil.extractLockState(o));

        synchronized (o) {
            System.out.println("When used to lock, object is: " + HeaderUtil.extractLockState(o));
            o.hashCode();
            System.out.println("After calculating hash, object is: " + HeaderUtil.extractLockState(o));
        }

        System.gc();
        System.out.println("After synchronisation object is: " + HeaderUtil.extractLockState(o));
    }
}
