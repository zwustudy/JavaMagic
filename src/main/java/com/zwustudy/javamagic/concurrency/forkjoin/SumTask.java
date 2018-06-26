/**
 * 
 */
package com.zwustudy.javamagic.concurrency.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author zwustudy
 *
 */
public class SumTask extends RecursiveTask<Long> {
	
	private static final long serialVersionUID = -8725982121134569427L;
	
	private static final long THRESHOLD = 200000;//阈值
	
	private long start;
	
	private long end;

	public SumTask(long start, long end) {
		this.start = start;
		this.end = end;
	}


	@Override
	protected Long compute() {
		long sum = 0;
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			for (long i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			long middle = (start + end) / 2;
			SumTask leftTask = new SumTask(start, middle);
			SumTask rightTask = new SumTask(middle + 1, end);
			leftTask.fork();
			rightTask.fork();
			long leftSum = leftTask.join();
			long rightSum = rightTask.join();
			return leftSum + rightSum;
		}
		return sum;
	}

	public static void main(String[] args) {
		
		long start = 1L;
		long end = 100000000000L;
		long now = System.currentTimeMillis();
		long sum = 0;
		for (long i = start; i <= end; i++) {
			sum += i;
		}
		System.out.println("通过单线程的方式计算" + start + "到" + end + "之间整数求和得到" + sum + "，所花时间:" + (System.currentTimeMillis() - now) + "ms");
		
		now = System.currentTimeMillis();
		
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		SumTask sumTask = new SumTask(start, end);
		Future<Long> future = forkJoinPool.submit(sumTask);
		try {
			sum = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("通过fork/join的方式计算" + start + "到" + end + "之间整数求和得到" + sum + "，所花时间:" + (System.currentTimeMillis() - now) + "ms");
	}
}
