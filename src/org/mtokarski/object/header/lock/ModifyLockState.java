package org.mtokarski.object.header.lock;

import org.mtokarski.object.header.HeaderUtil;

/**
 * "Unlocking" object in synchronised block allows other thread to acquire lock.
 * This code without modifying header would cause a dead lock.
 */
public class ModifyLockState {

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        System.out.println("Object hash at the beginning: " + o.hashCode());
        System.out.println();
        synchronized (o) {
            System.out.println("Object is " + HeaderUtil.extractLockState(o));

            long originalHeader = HeaderUtil.setUnlocked(o);

            System.out.println("Original header: " + Long.toBinaryString(originalHeader));
            System.out.println("Header after \"unlocking\": " + Long.toBinaryString(HeaderUtil.getObjectHeader(o)));
            System.out.println("Object is " + HeaderUtil.extractLockState(o));
            System.out.println("Object hash: " + o.hashCode());
            System.out.println();
            Thread thread = new Thread(() -> {
                synchronized (o) {
                    System.out.println("Header in thread's synchronised block: " + Long.toBinaryString(HeaderUtil.getObjectHeader(o)));
                    System.out.println("Displaced header in thread's synchronised block: " + Long.toBinaryString(HeaderUtil.getDisplacedHeader(o)));
                    System.out.println("Object hash: " + o.hashCode());
                    System.out.println();
                }
            });
            thread.start();
            thread.join();
            System.out.println("Header after thread execution: " + Long.toBinaryString(HeaderUtil.getObjectHeader(o)));
            System.out.println();

            //without setting back valid header, application doesn't terminate
            HeaderUtil.setHeader(o, originalHeader);
        }
        System.out.println("Object hash: " + o.hashCode());
    }
}
