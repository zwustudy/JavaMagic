/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zwustudy
 * 自旋锁是采用让当前线程不停地的在循环体内执行实现的，当循环的条件被其他线程改变时 才能进入临界区
 * 自旋锁是在当前线程内不停的执行循环体，不进行线程状态的切换，因此也没有用户态内核态的切换，已经线程执行上下文的切换，性能很高。
 * 但是如果锁竞争激烈或者获取锁资源持有时间会比较长的情况下，大量线程在进行自旋，会浪费CPU，因此这里的权衡就是自旋消耗的性能与
 * 线程进行阻塞然后切换消耗的性能对比。
 * JDK引入了适应新自旋锁，开始有一个默认的自旋次数，如果本次通过自旋获取到了锁，那么下次就可以增加自旋次数，如果本次通过自旋没有
 * 获取到锁，那么后面就减少自旋次数，以此达到一个合适的自旋次数
 * 另外
 * 1、JDK也进行了锁的升级操作，如果通过自旋没有获取到锁，那么就升级成重量级锁
 * 2、CAS操作在竞争不激烈的情况下效果很好，如果在竞争激烈的情况下，会耗费CPU，compareAndSet一直不成功
 */
public class SpinLock {

	private AtomicReference<Thread> lockHolder = new AtomicReference<>();

	public void lock() {
		Thread currThread = Thread.currentThread();
		//使用CAS操作，lock函数将owner设置为当前线程，并且预测原来的值为null。
		//当有第二个线程调用lock操作时由于owner值不为空，导致循环一直被执行，直至第一个线程调用unlock函数将owner设置为null，第二个线程才能进入临界区。
		while (!lockHolder.compareAndSet(null, currThread));
	}

	public void unlock() {
		Thread currThread = Thread.currentThread();
		//unlock函数将owner设置为null，并且预测值为当前线程
		while(!lockHolder.compareAndSet(currThread, null));
	}
	
	public static void main(String[] args) {
		final SpinLock lock = new SpinLock();
		final Data data = new Data();
		for (int i = 1; i <= 100; i++) {
			final int adder = i;
			Thread t = new Thread(() -> {
				lock.lock();
				String name = Thread.currentThread().getName();
				data.setValue(data.getValue() + adder);
				System.out.println(name + " 对data执行了加\t" + adder + "\t操作，data的value=" + data.getValue());
				lock.unlock();
			}, "child thread \t" + i + "\t");
			t.start();
		}
	}
}
