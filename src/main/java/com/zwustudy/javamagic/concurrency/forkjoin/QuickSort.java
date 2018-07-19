/**
 * 
 */
package com.zwustudy.javamagic.concurrency.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author minmin
 *
 */
public class QuickSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int LENGTH = 50000000 * 2;
		int[] array1 = new int[LENGTH];
		int[] array2 = new int[LENGTH];
		int[] array3 = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			int n = (int) (Math.random() * Integer.MAX_VALUE) % Integer.MAX_VALUE;
			array1[i] = n;
			array2[i] = n;
			array3[i] = n;
		}

		// 简单快排
		long startTime = System.currentTimeMillis();
		sort(array1, 0, LENGTH - 1);
		System.out.println("original quick sort : " + (float) (System.currentTimeMillis() - startTime) / 1000f + "s");

		//Fork/Join并行计算版本
		startTime = System.currentTimeMillis();
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.submit(new SortAction(array1, 0, LENGTH - 1));
		fjp.shutdown();
		try {
			fjp.awaitTermination(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fork/Join sort : " + (float) (System.currentTimeMillis() - startTime) / 1000f + "s");

		//jdk排序，进行了各种优化（插排，TimSort，双轴，三轴快排）
		startTime = System.currentTimeMillis();
		Arrays.sort(array3);
		System.out.println("jdk sort : " + (float) (System.currentTimeMillis() - startTime) / 1000f + "s");
	}

	static void sort(int[] array, int left, int right) {
		if (right <= left) {
			return;
		}
		int index = partion(array, left, right);
		sort(array, left, index - 1);
		sort(array, index + 1, right);
	}

	static int partion(int[] array, int left, int right) {
		int key = array[right];
		int index = left - 1;
		for (int i = left; i < right; i++) {
			if (array[i] < key) {
				index++;
				swap(array, i, index);
			}
		}
		swap(array, index + 1, right);
		return index + 1;
	}
	
	static void swap(int[] array, int i, int j) {
		array[j] += array[i];
		array[i] = array[j] - array[i];
		array[j] -= array[i];
	}

	static class SortAction extends RecursiveAction {

		private static final long THRESHOLD = 200000L;

		private static final long serialVersionUID = 1L;

		int[] array = null;
		int left;
		int right;

		SortAction(int[] array, int left, int right) {
			this.array = array;
			this.left = left;
			this.right = right;
		}

		@Override
		protected void compute() {
			if (right <= left) {
				return;
			}
			int index = partion(array, left, right);
			
			//如果任务拆分粒度过小，任务拆分调度的性能已经抵不过单线程执行任务
			if (index - left <= THRESHOLD) {
				sort(array, left, index - 1);
			} else {
				new SortAction(array, left, index - 1).fork();
			}
			if (right - index <= THRESHOLD) {
				sort(array, index + 1, right);
			} else {
				new SortAction(array, index + 1, right).fork();
			}
		}
	}
}
