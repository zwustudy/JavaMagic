/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zwustudy
 *
 */
public class CLHLock {
	
	static class CLHNode {
		private volatile boolean isLocked = true;
	}
	
	private AtomicReference<CLHNode> tail;
	
	private ThreadLocal<CLHNode> preNode;
	
	private ThreadLocal<CLHNode> currNode;
	
	public CLHLock() {
		
		tail = new AtomicReference<>(new CLHNode());
		
		preNode = new ThreadLocal<CLHNode>() {
			protected CLHNode initialValue() {  
                return new CLHNode();  
            }  
		};
		
		currNode = new ThreadLocal<CLHNode>() {
			protected CLHNode initialValue() {  
                return null;  
            }
		};
	}
	
	public void lock() {
		CLHNode curr = currNode.get();
		curr.isLocked = true;
	}

	public void unlock() {
		
	}
}
