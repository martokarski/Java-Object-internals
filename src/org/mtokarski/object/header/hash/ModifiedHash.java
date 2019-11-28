package org.mtokarski.object.header.hash;

import org.mtokarski.object.header.HeaderUtil;

/**
 * Default hash implementation is using object's header to store value.
 * Thus it can be modified by accessing memory directly with use of {@link sun.misc.Unsafe}.
 */
public class ModifiedHash {

    public static final int FIRST_HASH = 222;
    public static final int SECOND_HASH = 333;

    public static void main(String... args) {
        Object o = new Object();

        HeaderUtil.saveHash(o, FIRST_HASH);
        System.out.println("Hash changed to:\t" + FIRST_HASH);

        int hashCode = o.hashCode();
        System.out.println("Object's hash is:\t" + hashCode);

        HeaderUtil.saveHash(o, SECOND_HASH);
        System.out.println("Hash changed to:\t" + SECOND_HASH);

        hashCode = o.hashCode();
        System.out.println("Object's hash is:\t" + hashCode);
    }
}
