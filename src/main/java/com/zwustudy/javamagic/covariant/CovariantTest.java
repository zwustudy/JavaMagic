/**
 * 
 */
package com.zwustudy.javamagic.covariant;

/**
 * @author minmin
 *
 */
public class CovariantTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testCovariant();
	}
	
	static void testCovariant() {
		Parent[] parents = new ChildA[3];
		parents[0] = new ChildB();
	}

	static class Parent {
		Parent() {
			System.out.println("I am parent.");
		}
	}
	
	static class ChildA extends Parent {
		ChildA() {
			System.out.println("I am childA.");
		}
	}
	
	static class ChildB extends Parent {
		ChildB() {
			System.out.println("I am childA.");
		}
	}
}


