package com.zwustudy.javamagic.quine;

/**
 * 
 * @author zwustudy
 * 程序不通过读文件的形式输出源代码本身
 */
public class Quine {
	
	public static void main(String[] args) {
		char q = 34;
		String[] ll = {
				"package com.zwustudy.javamagic.quine;",
				"",
				"/**",
				" * ",
				" * @author zwustudy",
				" *",
				" */",
				"public class Quine {",
				"	",
				"	public static void main(String[] args) {",
				"		char q = 34;",
				"		String[] ll = {",
				"				",
				"		};",
				"		for (int i = 0; i < 12; i++) {",
				"			System.out.println(ll[i]);",
				"		}",
				"		for (int i = 0; i < ll.length; i++) {",
				"			System.out.println(ll[12] + q + ll[i] + q + ',');",
				"		}",
				"		for (int i = 12; i < ll.length; i++) {",
				"			System.out.println(ll[i]);",
				"		}",
				"	}",
				"}"
		};
		for (int i = 0; i < 12; i++) {
			System.out.println(ll[i]);
		}
		for (int i = 0; i < ll.length; i++) {
			System.out.println(ll[12] + q + ll[i] + q + ',');
		}
		for (int i = 12; i < ll.length; i++) {
			System.out.println(ll[i]);
		}
	}
}
