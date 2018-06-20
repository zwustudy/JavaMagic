/**
 * 
 */
package com.zwustudy.javamagic.algorithms;

/**
 * @author zwustudy
 *
 */
public class KmpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String source = "bacbababadababacambabacaddababacasdsd";
		String target = "ababaca";
		source = "bacbababadababacABACDABADambabacaddababacasdsd";
		target = "ABACDABAD";
		System.out.println(Kmp.kmpSearch(source, target));
		System.out.println(Kmp.kmpSearch2(source, target));
	}
}
