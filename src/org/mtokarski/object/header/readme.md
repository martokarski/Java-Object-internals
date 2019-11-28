## Object's header

First bytes of each JVM object are used by object's header, which contains its essential internal information. Header's layout
is well described in a comment in JVM source code - [markOop.hpp](http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/87ee5ee27509/src/share/vm/oops/markOop.hpp).

Fragment of the comment with header layout:
>     32 bits:
>     \--------
>      hash:25 ------------>| age:4    biased_lock:1 lock:2 (normal object)
>      JavaThread*:23 epoch:2 age:4    biased_lock:1 lock:2 (biased object)
>      size:32 ------------------------------------------>| (CMS free block)
>      PromotedObject*:29 ---------->| promo_bits:3 ----->| (CMS promoted object)
>
>      64 bits:
>      \--------
>      JavaThread*:54 epoch:2 unused:1   age:4    biased_lock:1 lock:2 (biased object)
>      unused:25 hash:31 -->| unused:1   age:4    biased_lock:1 lock:2 (normal object)
>      PromotedObject*:61 --------------------->| promo_bits:3 ----->| (CMS promoted object)
>      size:64 ----------------------------------------------------->| (CMS free block)
>
>      unused:25 hash:31 -->| cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && normal object)
>      JavaThread*:54 epoch:2 cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && biased object)
>      narrowOop:32 unused:24 cms_free:1 unused:4 promo_bits:3 ----->| (COOPs && CMS promoted object)
>      unused:21 size:35 -->| cms_free:1 unused:7 ------------------>| (COOPs && CMS free block)

Sections:

* [Default hash](hash/readme.md)
* [Object's age](age/readme.md)
* [Locking mechanism](lock/readme.md)

Back: [Unsafe](../../unsafe/readme.md) Next: [Class pointer](../clazz/readme.md)

Up: [Readme](../../../../../readme.md)