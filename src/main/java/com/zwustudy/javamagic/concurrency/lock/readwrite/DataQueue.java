/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock.readwrite;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zwustudy
 *
 */
public class DataQueue {

	private int data;

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	public void get() {
		rwLock.readLock().lock();// 加读锁
		try {
			System.out.println(Thread.currentThread().getName() + " be ready to get data");
			Thread.sleep((long) (Math.random() * 1000));
			System.out.println(Thread.currentThread().getName() + " get the data:" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwLock.readLock().unlock();// 释放读锁
		}
	}

	public void set(int data) {
		rwLock.writeLock().lock();// 加写锁
		try {
			System.out.println(Thread.currentThread().getName() + " be ready to write data");
			Thread.sleep((long) (Math.random() * 1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName() + " has written the data:" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();// 释放写锁
		}
	}

}
