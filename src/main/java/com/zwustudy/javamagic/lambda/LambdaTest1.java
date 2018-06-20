/**
 * 
 */
package com.zwustudy.javamagic.lambda;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zwustudy
 *
 */
public class LambdaTest1 {

	public static void main(String args[]){
	    
		List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
	 
		System.out.println("Languages which starts with J :");
	    filter(languages, (str)->str.startsWith("J"));
	 
	    System.out.println("Languages which ends with a ");
	    filter(languages, (str)->str.endsWith("a"));
	 
	    System.out.println("Print all languages :");
	    filter(languages, (str)->true);
	 
	    System.out.println("Print no language : ");
	    filter(languages, (str)->false);
	 
	    System.out.println("Print language whose length greater than 4:");
	    filter(languages, (str)->str.length() >= 4);
	    
	    Predicate<String> condition1 = str -> str.startsWith("J");
	    
	    //default 方法,流API,
	    languages.stream().filter(condition1.and(str -> str.length() >= 4)).forEach((n) -> System.out.println("nName, which starts with 'J' and four letter long is : " + n));;
	
	    
	    // 创建一个字符串列表，每个字符串长度大于2
	    List<String> filtered = languages.stream().filter(x -> x.length() > 3).collect(Collectors.toList());
	    System.out.printf("Original List : %s, filtered list : %s %n", languages, filtered);
	    
	    
	    // 将字符串换成大写并用逗号链接起来
	    List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
	    String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
	    System.out.println(G7Countries);
	    
	    // 用所有不同的数字创建一个正方形列表
	    List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
	    List<Integer> distinct = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
	    System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
	    
	    
	    //获取数字的个数、最小值、最大值、总和以及平均值
	    List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
	    IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
	    System.out.println("Highest prime number in List : " + stats.getMax());
	    System.out.println("Lowest prime number in List : " + stats.getMin());
	    System.out.println("Sum of all prime numbers : " + stats.getSum());
	    System.out.println("Average of all prime numbers : " + stats.getAverage());
	}
	 
	public static void filter(List<String> names, Predicate<String> condition) {
	    for(String name: names)  {
	        if(condition.test(name)) {
	            System.out.println(name + " ");
	        }
	    }
	}

}
