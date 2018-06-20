/**
 * 
 */
package com.zwustudy.javamagic.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * @author zwustudy
 * for map/reduce test
 */
public class LambdaTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testMap();
		testReduce();
	}
	
	static void testMap() {
		
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		// 不使用lambda表达式为每个订单加上12%的税
		for (Integer cost : costBeforeTax) {
		    double price = cost + .12*cost;
		    System.out.println("old map:" + price);
		}
		 
		// 使用lambda表达式
		costBeforeTax.stream().map((cost) -> cost + .12*cost).forEach(n->{System.out.println("new map:" + n);});
	}
	
	static void testReduce() {
		// 为每个订单加上12%的税
		// 老方法：
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		double total = 0;
		for (Integer cost : costBeforeTax) {
		    double price = cost + .12*cost;
		    total = total + price;
		}
		System.out.println("Total : " + total);
		 
		double bill = costBeforeTax.stream().map((cost) -> cost + .12*cost).reduce((sum, cost) -> sum + cost).get();
		System.out.println("Total : " + bill);
	}

}
