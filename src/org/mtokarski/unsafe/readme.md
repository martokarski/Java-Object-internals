## Unsafe class

*sun.misc.Unsafe* is an internal class, which isn't included in a standard Java library. 
This class **should not be used** in any production code. It can be removed in future versions of JVM
and even now it can be accessed only by using reflection mechanism. However this class is still
heavily used by Java and JVM internals, some 3rd party libraries are also using it.

Unsafe class has many features. Among them most useful in this project is ability to read and modify native memory.

Most of examples presented in this repository are using *Unsafe* class, but only to present internal mechanisms.
There is no guarantee any of code in this project will work.

### Native memory access

*Unsafe* class allows to directly access native memory populated by Java objects and JVM internal structures.
It allows to read, modify and allocate space without usual limits.

Keyword *final* applied to field prevents changing it after initialization. Such fields look exactly the same in native memory.
It means that fragment of memory representing a field can be modified. The same result may be achieved by using reflection API.
[Modify final field example](ModifyFinalField.java). The same rules apply to fields with limited access
(by using access modifiers e.g. *private*). [Access private field example](GetPrivateField.java)

Fields can be modified by using atomic methods (e.g. getAndAddInt, compareAndSwap). The same result can be also achieved with atomic classes,
like *AtomicInteger*.

Native memory can be allocated manually. Created block isn't under GC control, so its address never changes, but it should be
freed when no longer used. Similar results can be achieved by using "native" *ByteBuffer*. [Native allocation example](NativeMemory.java)

### Creating object without constructor

Normal object's initialization is a multistep process. First *new* bytecode is called, to allocate memory for an object.
Then all constructor's parameters are put on stack and finally constructor is invoked by *invokespecial* bytecode. 
For example calling ```new Integer(1);``` will compile to:

> NEW java/lang/Integer  
> DUP  
> ICONST_1  
> INVOKESPECIAL java/lang/Integer.<init> (I)V  

[JVM Specification](https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-2.html#jvms-2.9.1) says that class can have 0
*instance initialization methods* (constructors), but such situation never happen in *Java* code, because compiler will always
create one if it isn't defined in code. *Unsafe* allows to skip constructor execution. In result all object's fields are left
uninitialized - with exception to the final primitive types, where value is known at the compilation time. [New instance creation example](NewInstance.java)

### Throwing checked exceptions

Java language requires that checked exception are declared in method signature or handled within it.
*Unsafe* allows to throw every *Throwable* object without this constraint. It's possible to achieve the same goal in Java code
thanks to generics. Due to *type erasure*, generics are removed after compilation (JVM doesn't support it), so it's possible to generate "unsafe" code.
Compiler cannot determine if code is valid or broken, so it will only raise a warning. [Throw checked exception example](ThrowCheckedException.java)

### Class definition

Usually classes are loaded by JVM itself, but *Unsafe* allows to load class directly from the byte array representing class. 
Class loaded by this mechanism has the same restrictions and can be used as any other class. Classes are loaded by class loaders.
JVM provides default ones, but anyone can create own implementation. Custom class loader can also define class from byte array.
[Load class from file](LoadClass.java)

### Using *Unsafe*

Despite fact that this class gives huge power, it should not be used in application code. Currently it is mostly used by internal API
and as such may be removed or changed in future releases. Operations possible with it are usually literally unsafe and dangerous.
Most capabilities **can be achieved by using different methods** (usually reflection).

Next: [Object's header](../object/header/readme.md)

Up: [Readme](../../../../readme.md)