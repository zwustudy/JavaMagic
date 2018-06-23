/**
 * 1、读写锁维护了一对锁，一个读锁，一个写锁。读写锁在同一时刻允许多个线程进行读操作，但是写线程访问过程中，所有的读线程和其他写线程均被阻塞。如此，并发性有了很大的提升。
 * 这样，在某些读远远大于写的场景中，读写锁能够提供比排它锁更好的并发量和吞吐量。
 * 2、如果存在读锁，则写锁不能被获取，原因在于：读写锁要确保写锁的操作对读锁可见。如果允许读锁在已被获取的情况下对写锁的获取，那么正在运行的其他读线程就无法感知到当前写
 * 线程的操作。因此，只有等待其他读线程都释放了读锁，写锁才能被当前线程获取，而写锁一旦被获取，则其他读写线程的后续访问将会被阻塞。
 * 3、锁降级：指的是写锁降级成为读锁。具体操作是获取到写锁之后，在释放写锁之前，要先再次获取读锁。为什么要这样处理呢，答案就是为了保证数据可见性。如果当前线程不获取读锁
 * 而是直接释放写锁，假设此刻另一个线程（记作T）获取了写锁并修改了数据，那么当前线程无法感知线程T的数据更新。如果当前线程获取读锁，即遵循锁降级的步骤，则线程T将会被阻塞
 * ，直到当前线程使用数据并释放读锁之后，T才能获取写锁进行数据更新。
 */
/**
 * @author zwustudy
 *
 */
package com.zwustudy.javamagic.concurrency.lock.readwrite;