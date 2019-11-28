package org.mtokarski.object.header.lock;

import org.mtokarski.object.header.HeaderUtil;

public class HeavyLock {

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        o.hashCode();
        System.out.println("At the beginning object is in state: " + HeaderUtil.extractLockState(o));
        long objectHeader = HeaderUtil.getObjectHeader(o);
        System.out.println("Object's header is: " + Long.toBinaryString(objectHeader));
        System.out.println();

        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println("Finally inside synchronised in thread");
            }
        });
        synchronized (o) {
            thread.start();
            Thread.sleep(1000);
            System.out.println("In synchronised block object is in state: " + HeaderUtil.extractLockState(o));
            System.out.println("Pointer to monitor is: " + Long.toBinaryString(HeaderUtil.getObjectHeader(o)));
            long displacedObjectHeader = HeaderUtil.getDisplacedHeader(o);
            System.out.println("Displaced header: " + Long.toBinaryString(displacedObjectHeader));
            if (objectHeader == displacedObjectHeader) {
                System.out.println("Displaced header matches original one");
            }
            System.out.println();
        }

        thread.join();
        //monitor is removed after gc
        System.gc();

        System.out.println();
        System.out.println("After synchronised block object is: " + HeaderUtil.extractLockState(o));
    }
}
