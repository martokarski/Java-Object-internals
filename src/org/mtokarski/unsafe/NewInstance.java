package org.mtokarski.unsafe;

import org.mtokarski.unsafe.UnsafeUtil;

public class NewInstance {

    public static void main(String[] args) throws InstantiationException {
        Instance instance = new Instance();
        System.out.println("New object has initialized field with default static value " + instance.fieldWithDefaultValue);
        System.out.println("New object has initialized field with default computed value " + instance.fieldWithComputedDefaultValue);
        System.out.println();

        instance = (Instance) UnsafeUtil.unsafe.allocateInstance(Instance.class);
        System.out.println("Without constructor execution field with default static value is " + instance.fieldWithDefaultValue);
        System.out.println("Without constructor execution field with default computed value is " + instance.fieldWithComputedDefaultValue);
    }

    private static class Instance {
        //in this case compiler knows value, so it replaces all usages with actual value
        private final int fieldWithDefaultValue = Integer.MAX_VALUE;
        private final int fieldWithComputedDefaultValue = returnValue();

        static int returnValue() {
            return Integer.MAX_VALUE;
        }
    }
}
