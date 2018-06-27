/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zwustudy
 * CLHLock是自旋锁的一种，能确保无饥饿性，提供先来先服务的公平性。
 * P -> C  当前线程永远在前面一个节点上自旋，不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋。
 * CLH在SMP的CPU架构下，性能是非常高的，但是在NUMA架构下，性能可能会大打折扣。
 * SMP架构：Symmetric Multi-Processor   对称多处理器架构，指的是服务器中的多个CPU对称工作，每个CPU访问内存地址需要相同的时间。
 * 主要特征是多个CPU共享内存、I/O等外设。正是由于这种架构的存在，SMP架构的服务器扩展能力有限，所有共享的环节都可能是SMP服务器扩展时的瓶颈，
 * 最受限制的就是内存。实验证明， SMP 服务器 CPU 利用率最好的情况是 2 至 4 个 CPU 。
 * NUMA架构：Non-uniform Memory Access 由于SMP在扩展能力上的限制，人们开始研究如何进行有效扩展从而构建大型系统的技术，NUMA架构
 * 就是其中之一。NUMA的架构的特点是，具有多个CPU模块，每个CPU模块的由几个CPU组成，CPU模块具有独立的本地内存、I/O槽口等，由于其节点之间可
 * 以通过互联模块进行连接和信息交互，因此每个CPU可以访问整个系统的内存。显然，访问本地内存的速度远远高于访问其他CPU模块的内存的速度，这也是非一
 * 致存储访问NUMA的由来。由于这个特点，为了更好的发挥系统性能，开发应用程序时，应该尽量避免不同CPU模块之间信息的交互
 * 显然如果CLHLock使用在NUMA架构下，每个线程分配的内存可能在不同的CPU模块之间，这里自旋访问的是前一个线程的内存节点，因此就可能效率受限。
 * 可以改进为MCSLock: C -> next, 永远在自己线程内存分配的节点进行自旋，当当前前程释放锁时，找到下一个节点，如果下个节点存在则把下一个
 * 节点的获取锁标志改为true,这样即使在NUMA架构下,也只有一次访问远程内存
 */
public class CLHLock {
	
	private static final long PARK_TIME = 1000l * 1000;
	
	static class CLHNode {
		private volatile boolean isLocked = false;
	}
	
	private AtomicReference<CLHNode> tail;
	
	private ThreadLocal<CLHNode> currNode;
	
	public CLHLock() {
		
		tail = new AtomicReference<>(new CLHNode());

		currNode = new ThreadLocal<CLHNode>() {
			protected CLHNode initialValue() {  
                return null;  
            }
		};
	}
	
	public void lock() {
		
		CLHNode curr = currNode.get();
		if (curr == null) {
			//第一次来
			curr = new CLHNode(); 
		}
		curr.isLocked = true;
		currNode.set(curr);
		
		//这里的tail的getAndSet操作确保了原子操作，来进行排队，先执行Set的人先排队
		CLHNode pre = tail.getAndSet(curr);
		while (pre.isLocked) {
			LockSupport.parkNanos(PARK_TIME);
		}
	}
	
	public void unlock() {
		CLHNode curr = currNode.get();
		curr.isLocked = false;
		currNode.remove();
	}
	
	public static void main(String[] args) {
		final CLHLock lock = new CLHLock();
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
