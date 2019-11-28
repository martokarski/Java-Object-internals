### Class pointer

First 8 bytes (in 64-bits environment) of an object are reserved for header. Next 8 bytes also have special meaning -
they contain pointer to the [Klass](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/574c3b0cf3e5/src/share/vm/oops/klass.hpp).
It is a native object that represents every class loaded by JVM. It is mirrored by Java implementation - *java.lang.Class*.

In general methods in *Java* are virtual. It means that actual method to execute is resolved only in runtime. 
Replacing class pointer in object will result in invoking methods implemented in replaced class - [Replace class example](ReplaceClass.java).
Of course it may cause throwing exception or even halting JVM, especially if switched classes have different set of methods.
[Replace class with custom method example](ReplaceClassWithCustomMethod.java)

Back: [Object's header](../header/readme.md) Next: [Fields](../fields/readme.md)

Up: [Readme](../../../../../readme.md)