# What's in the object?

This project contains examples showing how some JVM internals are implemented. It wasn't created as an exhaustive source of knowledge,
but rather as a (subjectively chosen) set of highlights that may be a start for looking deeper into internal mechanisms
we are using everyday :)

Word *Java* may be ambiguous. It may refer to the Java language, but it is also often used to describe JVM (Java Virtual Machine)
or even whole ecosystem - language, runtime environment, development tools and libraries. In this document *Java* always refers to language
and virtual machine is described as *JVM*.

Presented examples by their nature are closely coupled with specific JVM implementation. Virtual machine can be called JVM
if it is compatible with [JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/jvms8.pdf). However that specification
doesn't cover many internal elements, giving a free hand to actual implementers. Thanks to that approach, 
multiple JVM implementations are existing on the market - they can make their own optimizations based on the eventual use cases.

JVM implementation used to test presented examples is **OpenJDK 8**. [OpenJDK](https://openjdk.java.net/) is an open source project
that is very similar to proprietary "HotSpot" JVM from Oracle. 

This project is divided into sections:

* [Unsafe class](src/org/mtokarski/unsafe/readme.md)
* [Object's header](src/org/mtokarski/object/header/readme.md)
    * [Default hash](src/org/mtokarski/object/header/hash/readme.md)
    * [Object's age](src/org/mtokarski/object/header/age/readme.md)
    * [Locking mechanism](src/org/mtokarski/object/header/lock/readme.md)
* [Class pointer](src/org/mtokarski/object/clazz/readme.md)
* [Fields](src/org/mtokarski/object/fields/readme.md)

Direct source of information is linked directly in the text. However I would like to link to other sources that helped me create this project:

1. [Java Language and Virtual Machine Specifications](https://docs.oracle.com/javase/specs/)
2. [How does the default hashCode() work?](https://srvaroa.github.io/jvm/java/openjdk/biased-locking/2017/01/30/hashCode.html)
3. [Synchronization and Object Locking](https://wiki.openjdk.java.net/display/HotSpot/Synchronization)
4. [Biased Locking in HotSpot](https://blogs.oracle.com/dave/biased-locking-in-hotspot)
5. [Evaluating and improving biased locking in the HotSpot virtual machine](http://www.diva-portal.org/smash/get/diva2:754541/FULLTEXT01.pdf)
6. [Default hashcode in java and biased locking](https://blog.gotofinal.com/java/2017/10/08/java-default-hashcode.html)
7. [OpenJDK 8 sources](https://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/)