package org.mtokarski.object.header.hash;

import org.mtokarski.object.header.HeaderUtil;

/**
 * Only default implementation {@link Object#hashCode()} stores value in object's header.
 * This field cannot be used in overrode method.
 * <br>
 * <br>
 * Default implementation can be always invoked by using method - {@link System#identityHashCode(Object)}.
 */
public class CustomHash {

    public static void main(String... args) {
        Object o = new ObjectWithCustomHash();

        long hashCode = o.hashCode();
        System.out.println("Hash code:\t\t\t" + hashCode);

        hashCode = HeaderUtil.getObjectHash(o);
        System.out.println("Value in header:\t" + hashCode);

        hashCode = System.identityHashCode(o);
        System.out.println("Default hash code:\t" + hashCode);

        hashCode = HeaderUtil.getObjectHash(o);
        System.out.println("Value in header:\t" + hashCode);
    }

    /**
     * Object with overrode hashCode method
     */
    static class ObjectWithCustomHash {

        @Override
        public int hashCode() {
            return 12345;
        }
    }
}
