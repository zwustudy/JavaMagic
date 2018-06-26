/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zwustudy
 * Ticket锁用来解决锁定顺序的问题，但是在多核CPU上，但是自旋一直调用serviceIndex.get()方法，必须要到主存读取，并阻止其他CPU修改
 */
public class TicketLock {

	//记录当前持有锁的index
	private AtomicInteger serviceIndex = new AtomicInteger();
	//记录每个线程来请求锁的顺序
	private AtomicInteger ticketIndex = new AtomicInteger();
	//保存当前线程访问的顺序
	private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<>();
	
	public void lock() {
		
		Integer threadTicket = LOCAL.get();
		if (threadTicket != null) {
			//说明获取锁的线程在进行重入
			return;
		}
		//否则重入会有大bug，一旦重入锁覆盖掉当前线程的ticket票据，
		int currTicket = ticketIndex.getAndIncrement();
		LOCAL.set(currTicket);
		while (currTicket != serviceIndex.get()) {
			//自旋
		}
	}
	
	public void unlock() {
		int currTicket = LOCAL.get();
		//释放掉锁时需要把存的当前线程的票据移除
		LOCAL.remove();
		//刚好唤醒下一个票据的线程，以达到访问顺序的关键
		serviceIndex.compareAndSet(currTicket, currTicket + 1);
	}
	
	public static void main(String[] args) {
		final TicketLock lock = new TicketLock();
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
