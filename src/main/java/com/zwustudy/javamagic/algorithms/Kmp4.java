/**
 * 
 */
package com.zwustudy.javamagic.algorithms;

/**
 * @author zwustudy
 *
 */
public class Kmp4 {
	
	
	public static int kmpSearch(String source, String target) {
		
		if (source == null || target == null) {
			throw new NullPointerException("source is null or target is null");
		}
		int[] next = cal_next(target);
		int k = 0;
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
		int[] next = new int[length];
	    next[0] = -1;  
	    System.out.println(0 + "\t" + next[0]);
	    int k = -1;  
	    int j = 0;  
	    while (j < length - 1) {  
	        //p[k]表示前缀，p[j]表示后缀  
	        if (k == -1 || target.charAt(j) == target.charAt(k)) {  
	            ++k; 
	            ++j;
	            next[j] = k;
	            System.out.println(j + "\t" + next[j]);
	        } else {
	        	k = next[k];
	        }
	    }  
	    return next;
	}
}
