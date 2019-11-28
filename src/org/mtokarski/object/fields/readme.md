### Object's fields

First bytes of each object are reserved for internal data (header and class pointer). Next to them are stored values of fields.
Types supported by the JVM are similar to the ones from Java language - [JVM data types](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.2).
Maximum size of single field is no more than 8 bytes. Longest primitive types are *long* and *double*. Objects are represented by reference,
which is 8 bytes long (in 64bits implementation).

In runtime location of the field is stored in form of an *offset* - number of bytes from object's header. *Unsafe* class allows
to read this offset, so field can be easily accessed directly. [Field access example](FieldAccess.java)

All types are stored in the form of bytes. Field type is stored in class definition, so with use of *Unsafe* class values
can be interpreted as different types. It may lead to misinterpretation, as integral (int, long etc.) and floating point (float, double)
are encoded differently, also size of types differ. Object's pointer is interpreted by JVM (e.g. to invoke method on it), 
so reading object from non-object type most probably will result in internal error (however interpreting pointer as long should be save).
[Different type example](DifferentFieldType.java)

Back: [Class pointer](../clazz/readme.md)

Up: [Readme](../../../../../readme.md)