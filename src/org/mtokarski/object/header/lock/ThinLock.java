package org.mtokarski.object.header.lock;

import org.mtokarski.object.header.HeaderUtil;

public class ThinLock {

    public static void main(String[] args) {
        Object o = new Object();
        o.hashCode();
        System.out.println("At the beginning object is in state: " + HeaderUtil.extractLockState(o));
        long objectHeader = HeaderUtil.getObjectHeader(o);
        System.out.println("Object's header is: " + Long.toBinaryString(objectHeader));
        System.out.println();

        synchronized (o) {
            System.out.println("In synchronised block object is in state: " + HeaderUtil.extractLockState(o));
            System.out.println("Pointer to lock record is: " + Long.toBinaryString(HeaderUtil.getObjectHeader(o)));
            long displacedObjectHeader = HeaderUtil.getDisplacedHeader(o);
            System.out.println("Displaced header: " + Long.toBinaryString(displacedObjectHeader));
            if (objectHeader == displacedObjectHeader) {
                System.out.println("Displaced header matches original one");
            }
        }

        System.out.println();
        System.out.println("After synchronised block object is: " + HeaderUtil.extractLockState(o));
    }
}
