### Hashcode

Object's hash is widely used in many places, e.g. *HashMap*. Every class can define its own implementation, but hashcode should never break contract -
[hashCode contract](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--). Breaking the contract may lead to unexpected behaviours.
Luckily default implementation exists and is used when method isn't overridden. 

Hash value is calculated on first execution and stored in the object's header. Every next execution simply reads value saved in the header. 
It improves performance, as actual calculation is executed only once, and ensures value doesn't change over time. [Default hash example](DefaultHash.java)

JVM has several hashcode algorithms ([get_next_hash()](http://hg.openjdk.java.net/jdk7/jdk7/hotspot/file/42ca4002efc2/src/share/vm/runtime/synchronizer.cpp#l246)).
It can be changed by adding JVM argument *-XX:hashCode=\<number>*, where number may be:

+ **0** - randomly generated value
+ **1** - function based on object's memory address
+ **2** - value is always 1
+ **3** - a sequence
+ **4** - object's memory address cast to int
+ **5** - thread state combined with xorshift - **default**

[Hash algorithm example](HashAlgorithm.java)

Algorithms described above are used only if object's class doesn't have custom implementation. Obviously in such case value
returned by custom method won't be saved in the header. Java allows to execute default implementation (even if method was implemented) by using method
*System.identityHashCode(object)*. [Custom hash example](src/org/mtokarski/object/header/hash/CustomHash.java)

Modified value (by *Unsafe*) will be used always when default *hashCode* 
implementation will be invoked. [Modified hash example](src/org/mtokarski/object/header/hash/ModifiedHash.java)

Next: [Object's age](../age/readme.md)

Up: [Object's header](../readme.md)
