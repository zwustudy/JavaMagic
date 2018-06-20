/**
 * 
 */
package com.zwustudy.javamagic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author zwustudy
 *
 */
public class ABAProblem {

	private static AtomicInteger atomicInt = new AtomicInteger(100);
	
    private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<Integer>(100, 0);
    
    public static void main(String[] args) throws InterruptedException {
        Thread intT1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(atomicInt.compareAndSet(100, 101));
                System.out.println(atomicInt.compareAndSet(101, 100));
            }
        });
        
        Thread intT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(atomicInt.compareAndSet(100, 101));//true
            }
        });
        
        intT1.start();
        intT2.start();
        intT1.join();
        intT2.join();
        
        Thread refT1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1));
                System.out.println(atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1));
            }
        });
        
        Thread refT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int beforeStamp = atomicStampedRef.getStamp();
                System.out.println("before sleep : stamp = " + beforeStamp);    // stamp = 0
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int afterStamp  = atomicStampedRef.getStamp();
                System.out.println("after sleep : stamp = " + afterStamp); //stamp = 2
                System.out.println(atomicStampedRef.compareAndSet(100, 101, beforeStamp, beforeStamp + 1)); // false 
                System.out.println(atomicStampedRef.compareAndSet(100, 101, afterStamp, afterStamp + 1)); // true
            }
        });
        
        refT1.start();
        refT2.start();
    }
    
}