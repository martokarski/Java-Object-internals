package org.mtokarski.object.header.hash;

/**
 * OpenJDK defines couple algorithms to calculate actual {@link Object#hashCode()}.
 * In order to change default implementation add VM parameter <b>-XX:hashCode=</b>
 * <ul>
 *     <li><b>0</b> - randomly generated value</li>
 *     <li><b>1</b> - function based on object's memory address</li>
 *     <li><b>2</b> - value is always 1</li>
 *     <li><b>3</b> - a sequence</li>
 *     <li><b>4</b> - object's memory address cast to int</li>
 *     <li><b>5</b> - thread state combined with xorshift</li>
 * </ul>
 */
public class HashAlgorithm {

    public static void main(String... args) {
        for (int i = 0; i < 100; i++) {
            Object o = new Object();
            System.out.println(o.hashCode());
        }
    }
}
