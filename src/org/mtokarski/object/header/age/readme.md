### Object's age

It is common knowledge, that JVM uses Garbage Collector algorithms to automatically manage available memory. All algorithms
used in JVM are generational - in short newly created objects are kept in "Young Generation" and if they live long enough,
they are moved to the "Old Generation". That division is based on "Weak Generational Hypothesis" - derived from real observations,
showing that many objects tend to have short life, but some of them can live for a long time - possibly as long as application itself.

> Info: for examples mentioned in this section any other GC algorithms than default one (ParallelGC) is advised, e.g. *-XX:+UseSerialGC*.
> Default GC uses different policy to promote objects [[1]](https://www.oracle.com/technetwork/java/visualgc-136680.html#SurvivorAgeHistogram   )

Most of the GC algorithms "promotes" objects to the old gen, based on the number of "minor GC" object experienced. This number
is stored on 4 bits in the object's header, so maximum possible age is 15. Age of objects stored in old gen is not tracked.
JVM adjust promotion age trying to optimise memory and time consumption, under extreme conditions objects may be promoted 
instantly after creation. By setting *-XX:MaxTenuringThreshold=\<value from 1 to 15>* maximum age may be limited, but JVM can
always set smaller one. [Age example](UnsafeAge.java)

Thanks to generational memory JVM can focus on cleaning young gen, where probably many objects are already "dead". However
sometimes it may happen that application use all its memory. Before JVM throws *OutOfMemoryError*, first so called "Full GC"
is executed (in fact JVM will throw OOME only if certain requirements are met, e.g. too small part of memory was reclaimed in past couple "Full GC").
Full GC can be also executed manually (e.g. MBeans) or programmatically (not advised). In such case all unnecessary objects are removed from memory and
all remaining object are instantly promoted to the old generation. [Full GC example](FullGcAge.java)

Playing with JVM internals is always a bad idea, but thanks to *Unsafe* class we can modify object's age. It allows to "confuse"
GC algorithm and store object in the young generation indefinitely. [Always young example](AlwaysYoungAge.java)

Back: [Default hash](../hash/readme.md) Next: [Locking mechanism](../lock/readme.md)

Up: [Object's header](../readme.md)
