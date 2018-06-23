/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock.readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zwustudy
 *
 */
public class DataCache {

	//加volatile关键字保证可见性。
	private volatile Map<String, String> cacheMap = new HashMap<>();

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	public String getValue(String key) {
		rwLock.readLock().lock();
		try {
			String value = cacheMap.get(key);
			System.out.println(Thread.currentThread().getName() + " get the value:" + value);
			return value;
		} finally {
			rwLock.readLock().unlock();
		}
	}
	
	public void setValue(String key, String value) {
		
		rwLock.writeLock().lock();
		try {
			rwLock.readLock().lock();// 将锁降级，这里跟下一句不能写反，即要求：先获取到读锁，再释放写锁
			//此时写锁没有释放，所有的读写线程都处于阻塞状态
			System.out.println(Thread.currentThread().getName() + " set the value:" + value);
			rwLock.writeLock().unlock();
			//这里将写锁降级为读锁，配合volatile关键字使用，在读锁未释放之前，其它线程已经可以读到准确数据，但是写操作任然是阻塞状态
			cacheMap.put(key, value);
		} finally {
			rwLock.readLock().unlock();
		}
	}
}
