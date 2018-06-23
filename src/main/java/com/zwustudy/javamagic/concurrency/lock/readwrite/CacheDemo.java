/**
 * 
 */
package com.zwustudy.javamagic.concurrency.lock.readwrite;

/**
 * @author zwustudy
 *
 */
public class CacheDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DataCache dc = new DataCache();
        
		for (int i = 0; i < 100; i++) {
            final int index = i;
        	if (i % 10 == 0) {
        		new Thread(() -> dc.setValue("key", "value" + index)).start();
        	} else {
        		new Thread(() -> dc.getValue("key")).start();
        	}
        }
	}

}
