package org.mtokarski.object.header.hash;

import org.mtokarski.object.header.HeaderUtil;

/**
 * Default hash is calculated in first execution of default method and then stored in object's header.
 */
public class DefaultHash {

    public static void main(String... args) {
        Object o = new Object();

        long hash = HeaderUtil.getObjectHash(o);
        System.out.println("hash stored in header:\t\t" + hash);

        hash = System.identityHashCode(o);
        System.out.println("Hash from default method:\t" + hash);

        hash = HeaderUtil.getObjectHash(o);
        System.out.println("Hash stored in header:\t\t" + hash);
    }
}
