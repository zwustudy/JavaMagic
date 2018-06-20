/**
 * 
 */
package com.zwustudy.javamagic.lambda;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author zwustudy
 *
 */
public class LambdaTest0 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("____________________test1 start_______________________");
		test1();
		System.out.println("____________________test1 end_______________________");
		System.out.println("____________________test2 start_______________________");
		test2();
		System.out.println("____________________test2 end_______________________");
	}
	
	static void test1() {
		// Java 8之前：
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    System.out.println("Before Java8, too much code for too little to do");
		    }
		}).start();
		
		//Java 8方式：
		new Thread(() -> System.out.println("In Java8, Lambda expression rocks !!") ).start();
		
		LambdaInterface li = (a, b) -> {System.out.println("My FunctionalInterface Test");return a + b;};
		System.out.println("forLambdaTest:" + li.add(20, 30));
		
		forLambdaTest((c, d) -> {System.out.println("My FunctionalInterface Test"); return c + d;});
		
		forLambdaTest(LambdaTest0::methodReference2);
	}
	
	static void forLambdaTest(LambdaInterface li) {
		int x = 10, y = 20;
		System.out.println("forLambdaTest:" + li.add(x, y));
	}
	
	
	
	static void test2() {
		// Java 8之前：
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		for (String feature : features) {
		    System.out.println(feature);
		}
		// Java 8方式1：
		features.forEach(n -> System.out.println(n));
		// Java 8方式2：一个参数时圆括号可有可无
		features.forEach((n) -> System.out.println(n));
		features.forEach(System.out::println);
		
		features.forEach(LambdaTest0::methodReference1);
		
		//构造函数作为函数引用
		Set<String> ss = transferElements(features, HashSet::new);
		ss.forEach(System.out::println);
	}
	
	static void methodReference1(Object o) {
		System.out.println("My MethodReference1 Test:" + o);
	}
	
	static int methodReference2(int x, int y) {
		System.out.println("My MethodReference2 Test:");
		return x + y;
	}
	
	
	public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>> DEST transferElements(SOURCE sourceColletions, Supplier<DEST> colltionFactory) {
        DEST result = colltionFactory.get();
        for (T t : sourceColletions) {
            result.add(t);
        }
        return result;
    }
}
