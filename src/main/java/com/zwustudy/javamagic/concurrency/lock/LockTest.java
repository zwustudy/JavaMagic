/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zwustudy
 * 用来测试lock接口中lock方法和lockInterruptibly的效果：
 * 测试结果：
 * lock()方法获取锁失败后，线程会被阻塞，哪怕别的线程调用该线程的interrupt方法，该线程任然被阻塞，不会被感知
 * lockInterruptibly()方法有抛出一个InterruptedException异常获取锁失败后，线程会被阻塞；不同的是如果别的线程调用该线程的interrupt方法
 * 该线程会被激活, 进入InterruptedException处理流程
 * 线程进入sleep方法或者wait/await方法也会出一个InterruptedException, 当别的线程调用该线程的interrupt方法，
 * 线程也会被激活, 进入InterruptedException处理流程
 */
public class LockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		testLock();
		
		testLockInterruptibly();
		
		testLockInterruptiblyWhileSleep();
		
		Thread.sleep(1000000);
	}
	
	static void testLock() throws Exception {

		final Lock lock = new ReentrantLock();
        lock.lock();
        Thread t1 = new Thread(()-> {
        	lock.lock();
            System.out.println(Thread.currentThread().getName() + " interrupted.");
        }, "child thread -1");
        
        t1.start();
        Thread.sleep(1000);
        
        t1.interrupt();
        System.out.println(Thread.currentThread().getName() + " testLock come there.");
	}
	
	static void testLockInterruptibly() throws Exception {

		final Lock lock = new ReentrantLock();
		lock.lock();
        Thread t1 = new Thread(()-> {
        	try {
				lock.lockInterruptibly();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " interrupted.");
				e.printStackTrace();
			}
        	System.out.println(Thread.currentThread().getName() + " run come there.");
        }, "child thread 1");
        
        t1.start();
        Thread.sleep(1000);
        
        t1.interrupt();
        
        System.out.println(Thread.currentThread().getName() + " testLockInterruptibly come there.");
	}
	
	static void testLockInterruptiblyWhileSleep() throws Exception {

		final Lock lock = new ReentrantLock();
        lock.lock();
        Thread t1 = new Thread(()-> {
        	try {
        		System.out.println(Thread.currentThread().getName() + " will sleep 2 seconds.");
        		Thread.sleep(2000);
        		
				lock.lockInterruptibly();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " interrupted.");
				e.printStackTrace();
			}
        	System.out.println(Thread.currentThread().getName() + " run come there.");
        }, "child thread 1");
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        System.out.println(Thread.currentThread().getName() + " testLockInterruptiblyWhileSleep come there.");
	}
}
