/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zwustudy
 * 这里主要用来比较synchronized加锁和ReentrantLock异同：都是可以重入的锁
 * 两者都是同一个线程没进入一次，锁的计数器都自增1，所以要等到锁的计数器下降为0时才能释放锁。
 * 锁的实现：
 * 		Synchronized是依赖于JVM实现的，而ReenTrantLock是JDK实现的
 * 性能的区别：
 * 		在Synchronized优化以前，synchronized全都是以重量级锁的形式存在，性能比ReentrantLock差很多，但是自从synchronized引入偏向锁
 * 、轻量级锁（自旋锁）之后，两者的性能已经差不多。如果两种方法都可用的情况下，官方甚至都建议使用synchronized，其实synchronized底层的优化也是使用
 * CAS机制。都是在用户态就把加锁问题解决，避免进入内核态的线程阻塞
 * 功能区别：
 * 		synchronized的使用方便，由编译器来操作加锁和释放，而ReenTrantLock需要手工声明来加锁和释放锁，为了避免忘记手工释放锁或者提前出异常释放失败
 * ，最好在finally中声明释放锁。
 * ReentrantLock的灵活性和细粒度由于synchronized
 * ReenTrantLock独有的能力：
 * 		1、可以指定公平锁还是非公平锁, synchronized只能是非公平锁
 * 		2、ReentrantLock提供了一个Condition（条件）类，可以用来实现分组唤醒需要唤醒的线程，而synchronized要么唤醒一个线程要么唤醒所有线程
 * 		3、ReentrantLock实现了Lock接口，自然锁的方式特别多，可以通过lock()、tryLock()、lockInterruptibly()，灵活性要好
 */
public class ReentrantLockTest {
	
	public static void main(String[] args) {
		//线程sleep线程休眠，wait/await会释放锁
		
		//testReentrantLockWhileSleep();
		//testReentrantLockWhileAwait();
		//testSynchronizedWhileSleep();
		testSynchronizedWhileAwait();
	}
	
	static void testReentrantLockWhileSleep() {
		
		final ExecutorService exec = Executors.newFixedThreadPool(2);
        final ReentrantLock lock = new ReentrantLock();
        for(int index = 0; index < 2; index++) {
        	exec.submit(() -> {
                lock.lock();
                System.out.println("keep lock " + lock);
                try {
                	Thread.sleep(3000);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                } finally {
                	System.out.println("release lock " + lock.toString());
                	lock.unlock();
                }
            });
        }
        exec.shutdown();
	}
	
	static void testReentrantLockWhileAwait() {
		
		final ExecutorService exec = Executors.newFixedThreadPool(2);
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        for(int index = 0; index < 2; index++) {
        	exec.submit(() -> {
                lock.lock();
                System.out.println("keep lock " + lock);
                try {
                	condition.await(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                } finally {
                	System.out.println("release lock " + lock.toString());
                	lock.unlock();
                }
            });
        }
        exec.shutdown();
	}
	
	static void testSynchronizedWhileSleep () {
		
		final ExecutorService exec = Executors.newFixedThreadPool(2);
		final Object lock = new Object();
        for(int index = 0; index < 2; index++) {
        	exec.submit(() -> {
        		synchronized (lock) {
        			System.out.println("keep lock " + Thread.currentThread().getName());
        			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						System.out.println("release lock " + Thread.currentThread().getName());
					}
        		}
            });
        }
        exec.shutdown();
	}
	
	static void testSynchronizedWhileAwait () {
		
		final ExecutorService exec = Executors.newFixedThreadPool(2);
		final Object lock = new Object();
        for(int index = 0; index < 2; index++) {
        	final int x = index;
        	exec.submit(() -> {
        		synchronized (lock) {
        			System.out.println("keep lock " + Thread.currentThread().getName());
        			try {
						lock.wait(3000 * (x + 1));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						System.out.println("release lock " + Thread.currentThread().getName());
					}
        		}
            });
        }
        exec.shutdown();
	}
}
