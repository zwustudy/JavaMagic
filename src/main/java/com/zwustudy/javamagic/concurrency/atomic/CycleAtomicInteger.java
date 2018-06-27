/**
 * 
 */
package com.zwustudy.javamagic.concurrency.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zwustudy
 * 基于三种方式实现了一个CycleAtomicInteger，并测试三种效果的对比. 
 * 从结果来看第一种方式最优，第三种方式最差，强烈不建议使用第三种方式通过int溢出取绝对值值的方式来进行自选，并且没有控制自选的并发，这样高并发的情况下性能非常差
 * 实际上第二种方式性能比第一种方式基本不差，因为JVM会进行锁的优化，这是推荐的方式
 */
public class CycleAtomicInteger {
	
	private static final long PARK_TIME = 1000l * 1000;
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	
	private int range;
	
	private int index = 0;
	
	public CycleAtomicInteger(int range) {
		if (range < 2) {
			throw new IllegalArgumentException();
		}
		this.range = range;
	}

	//基于CAS机制来实现，高并发情况下，线程适当park
	public int next() {
		for (;;) {
			int c = atomicInteger.get();
			int next = (c + 1) % range;
			if (atomicInteger.compareAndSet(c, next)) {
				return c;
			} else {
				LockSupport.parkNanos(PARK_TIME);
			}
		}
	}
	
	/**
	 * 通过方法加锁的方式来实现线程安全，锁的优化交给JVM来实现。
	 * @return
	 */
	public synchronized int next_by_synchronized() {
		if (index >= range) {
			index = 0;
		}
		int next = index;
		index++;
		return next;
	}
	
	/**
	 * 通过AtomicInteger的原子自增来操作，，然后通过int溢出取绝对值来实现循环
	 * @return
	 */
	public int next_by_abs() {
		return Math.abs(atomicInteger.getAndIncrement()) % range;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CycleAtomicInteger cat = new CycleAtomicInteger(10);
		
		final int times = 200 * 1000 * 1000;
		Runnable runnable0 = () -> {
			for (int i = 0; i < times; i++) {
				cat.next();
			}
		};
		Runnable runnable1 = () -> {
			for (int i = 0; i < times; i++) {
				cat.next_by_synchronized();
			}
		};
		
		Runnable runnable2 = () -> {
			for (int i = 0; i < times; i++) {
				cat.next_by_abs();
			}
		};
		
		List<Thread> threads0 = new ArrayList<>();
		List<Thread> threads1 = new ArrayList<>();
		List<Thread> threads2 = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			threads0.add(new Thread(runnable0));
			threads1.add(new Thread(runnable1));
			threads2.add(new Thread(runnable2));
		}
		
		long start = System.currentTimeMillis();
		threads0.forEach(t -> t.start());
		threads0.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
		
		start = System.currentTimeMillis();
		threads1.forEach(t -> t.start());
		threads1.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
		
		start = System.currentTimeMillis();
		threads2.forEach(t -> t.start());
		threads2.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
	}
}
