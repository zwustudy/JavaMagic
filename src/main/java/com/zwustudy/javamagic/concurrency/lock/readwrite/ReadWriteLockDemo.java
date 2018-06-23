/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock.readwrite;

import java.util.Random;

/**
 * @author zwustudy
 *
 */
public class ReadWriteLockDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataQueue queue = new DataQueue();
		for (int i = 1; i < 10; i++) {
			// 启动线程进行读操作
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						queue.get();
					}
				}

			}).start();

			// 启动线程进行写操作
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						queue.set(new Random().nextInt(10000));
					}
				}
			}).start();
		}
	}

}
