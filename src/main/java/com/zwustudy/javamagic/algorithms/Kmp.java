/**
 * 
 */
package com.zwustudy.javamagic.algorithms;

/**
 * @author zwustudy
 *
 */
public class Kmp {
	
	
	public static int kmpSearch(String source, String target) {
		
		if (source == null || target == null) {
			throw new NullPointerException("source is null or target is null");
		}
		int[] next = cal_next(target);
		int k = -1;
		for (int i = 0; i < source.length(); i++) {
	        while (k > -1 && target.charAt(k + 1) != source.charAt(i))//ptr和str不匹配，且k>-1（表示ptr和str有部分匹配）
	        	k = next[k];//往前回溯
	        if (target.charAt(k + 1) == source.charAt(i))
	            k = k + 1;
	        if (k == target.length() - 1) {
	        	//说明k移动到ptr的最末端
	        	//k = -1;//重新初始化，寻找下一个
	        	//i = i - plen + 1;//i定位到该位置，外层for循环i++可以继续找下一个（这里默认存在两个匹配字符串可以部分重叠），感谢评论中同学指出错误。
	            return i - target.length() + 1;//返回相应的位置
	        }
	    }
	    return -1;
	}
	
	private static int[] cal_next(String target) {
		
		int length = target.length();
		if (length == 0) {
			return new int[0];
		}
		int[] next = new int[length];
		int k = -1;
		next[0] = k;
		System.out.println(0 + "\t" + k);
		for (int i = 1; i < length; i++) {
		
			while (k > -1 && target.charAt(k + 1) != target.charAt(i)) {
				k = next[k];
			}
			
			if (target.charAt(k + 1) == target.charAt(i)) {
				k = k + 1;
			}
			next[i] = k;
			System.out.println(i + "\t" + next[i]);
		}
		return next;
	}
	
	public static int kmpSearch2(String source, String target) {
		
		if (source == null || target == null) {
			throw new NullPointerException("source is null or target is null");
		}
		int i = 0, j = 0;
		int sourceLength = source.length();
		int targetLength = target.length();
		int[] next = cal_next2(target);
		while (i < sourceLength && j < targetLength) {
			if (j == -1 || source.charAt(i) == target.charAt(j)) {
				i++;
				j++;
			} else {
				j = next[j];
			}
		}
		if (j == targetLength) {
			return i - j;
		}
		return -1;
	}
	
	private static int[] cal_next2(String target) {
		
		int length = target.length();
		if (length == 0) {
			return new int[0];
		}
		int[] next = new int[length];
		int k = -1;
		next[0] = k;
		int j = 0;
		System.out.println(0 + "\t" + k);
		while (j < length - 1) {
			if (k == -1 || target.charAt(k) == target.charAt(j)) {
				k++;
				j++;
				next[j] = k;
				System.out.println(j + "\t" + next[j]);
			} else {
				k = next[k];
			}
		}
		return next;
	}
	
}
