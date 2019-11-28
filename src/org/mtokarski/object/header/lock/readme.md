### Locking mechanism

Each JVM object can be used to lock synchronised block of code. Ensuring that only one thread will invoke given piece of code
in a multithreaded environment is not an easy task and may influence performance significantly. JVM contains complicated
mechanism to balance performance in different scenarios.

Synchronisation diagram: ([source](https://wiki.openjdk.java.net/display/HotSpot/Synchronization))

![locking scheme](../../../../../../resources/Synchronization.gif)

#### Unlocked object

Object always start in "unlocked" state, so last 2 bits of it's header are set to **01**. This state indicates that object
is not used by any thread to lock.

#### Thin lock

When thread wants to acquire lock on a object, it performs CAS (Compare and Swap) operation on its header. If operation succeeded,
thread owns a lock and can invoke synchronised block. Value presented in header is a pointer to the "lock record" - structure
that contains information about lock. First bytes of lock record always contain copy of an original header. [Thin lock example](ThinLock.java)

Pointer to the lock record is always aligned by 2 bits, so last 2 bits are always **00**. Lock record is stored in Thread's
stack frame, so if thread tries to acquire lock again (nested lock), it just checks if lock record exists in its own stack frame.

Implementation of acquiring thin lock can be found in [slow_lock() method](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/574c3b0cf3e5/src/share/vm/runtime/synchronizer.cpp#l226)

#### Heavy lock

Thin locks are quite efficient, however they can be used only if one thread acquires lock at a time. Whole concept of synchronisation
was created to stop other threads. That scenario requires heavy lock - represented by ObjectMonitor structure. It stores
object's header, pointer to current thread and list of threads waiting to acquire lock. [Heavy lock example](HeavyLock.java)

Process of creating object monitor is called inflating. Depending on the initial state of the object different actions are taken:
* header points to object monitor (last 2 bits of header have to be set to **10**) - pointer to object monitor is simply returned.
* thin lock is already in place - all bits in object's header are set to 0 - indicating transient state "INFLATING" - by using CAS operation.
if operation succeeded new object monitor is created and displaced header (from lock record) and lock owner (thread or lock record) is saved.
Then pointer to object monitor is saved in header and returned.
* inflating state - thread is spinning until object is no longer being inflated (some other thread finished inflating process)
* object is unlocked - new object monitor is created and pointer is placed in object's header with CAS operation; if operation
succeeded object monitor is returned.
Inflating process can be started by many threads at the same time, so above actions are invoked in a loop until one of them
succeeded.

Implementation of creating object monitor can be found in [inflate() method](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/574c3b0cf3e5/src/share/vm/runtime/synchronizer.cpp#l1197)

#### Biased lock

This type of lock is the youngest one - it was introduced in Java 6 and is protected by [US patent](https://patents.google.com/patent/US7814488).
It bases on an assumption that many locks on objects are usually acquired by single threads at runtime. So effectively there
is no need to inflate locks. Thin locks are simple but require invoking CAS operation on enter to synchronised block (excluding nested locks).
These type of operations is costly (sources are claiming it was improved in modern CPUs). To avoid multiple operations,
biased lock always points to a thread owner, so it is effectively locked even outside synchronised blocks.

Biasing is enabled by default, but can be disabled with flag *-XX:-UseBiasedLocking*. Due to historical performance issues,
mechanism is disabled for 4 seconds after startup. It can be adjusted by using *-XX:BiasedLockingStartupDelay=<delay in ms>*.
In presented examples it should be set to 0, in order to see biased locking in action.

Object is biased locked if last 2 bits are in unlocked state (**01**) and 3rd bit is set to **1**. Location of hash in header
is used to store pointer to the owning thread. At the beginning pointer is set to 0, so object is anonymously locked (ready for bias locking).
When first thread tries to acquire lock, pointer to it is put to the header by CAS operation. If operation succeeded,
object is biased over a thread. Otherwise it falls to default locking mechanism. When thread enters synchronised block again,
it only reads header to check if it is object's owner. Thanks to that costly CAS operation is invoked only once.

This mechanism is very fast, but only in a happy scenario where lock is always acquired by single thread. If any other thread
tries to acquire lock, bias have to be revoked and return to standard mechanism. It is even more costly, because revoking bias
can be done only in "safe point" (periodical stops of all application threads, when JVM can perform operations, e.g. GC). [Biased locking example](BiasedLock.java)

JVM monitors number of revokes for each class and it can use heuristic algorithms to disable biased locking for certain classes.
It can also execute bulk revoking to return all objects of class to "anonymously locked" state. It also uses "epoch" bits
from header and compare them with the same bits from "prototype header" (copy of header stored in class object, used to initialize object's header).
If numbers saved in these bits are different, rebiasing happened and bias lock is no longer valid.

As mentioned earlier, pointer to the owning thread is written in place of hash. As a result objects with calculated identity
hash cannot be biased locked. Also if identity hash is calculated when object is already biased, it first have to fall back
to standard mechanism and only then hash can be calculated. [Biased locking and hash](BiasedLockWithHash.java)

Biased locking is implemented in method [fast_enter()](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/574c3b0cf3e5/src/share/vm/runtime/synchronizer.cpp#l168).

#### Modifying lock state

As proved in previous sections, class *Unsafe* gives direct access to the heap memory. Of course it allows to modify state
of lock. Every direct modification is **dangerous**, but because of complication and importance of locking mechanism, such operations
are **extremely dangerous**. Simple example shows that lock can be "acquired" by 2 threads. [Modify lock state example](ModifyLockState.java)

Back: [Object's age](../age/readme.md)

Up: [Object's header](../readme.md)
