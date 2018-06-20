/**
 * 
 */
package com.zwustudy.javamagic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwustudy
 *
 */
public class TreePrinter {
	
	static class Node {
		
		String value;
		
		Node left;
		
		Node right;
		
		public String toString() {
			return value;
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Node a = new Node();
		Node b = new Node();
		Node c = new Node();
		Node d = new Node();
		Node e = new Node();
		Node f = new Node();
		Node g = new Node();
		Node h = new Node();
		Node i = new Node();
		a.value = "a";
		a.left = b;
		a.right = c;
		
		b.value = "b";
		b.left = d;
		b.right = e;
		
		c.value = "c";
		c.right = f;
		
		d.value = "d";
		
		e.value = "e";
		e.left = g;
		
		f.value = "f";
		f.left = h;
		
		g.value = "g";
		
		h.value = "h";
		h.right = i;
		
		i.value = "i";
		print(a);
		printByList(a);
	}
	
	static void print(Node node) {
		List<Node> nodeList = nodeToList(node);
		for (Node _node : nodeList) {
			System.out.println(_node.value);
		}
		
		Map<Integer, List<Node>> visitMap = new LinkedHashMap<>();
		visit(node, 0, visitMap);
		for (Map.Entry<Integer, List<Node>> entry : visitMap.entrySet()) {
			System.out.println(entry.getKey()  + ":" + entry.getValue().toString());
		}
	}
	
	static void printByList(Node node) {
		if (node == null) {
			return;
		}
		List<Node> nodeList = new ArrayList<>();
		nodeList.add(node);
		int lastIndex = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			System.out.print(nodeList.get(i).value + " ");
			if (i == nodeList.size() - 1) {
				System.out.println();
				for (; lastIndex <= i; lastIndex++) {
					Node _node = nodeList.get(lastIndex);
					if (_node.left != null) {
						nodeList.add(_node.left);
					}
					if (_node.right != null) {
						nodeList.add(_node.right);
					}
				}
			}
		}
	}
	
	static List<Node> nodeToList(Node node) {
		List<Node> nodeList = new ArrayList<>();
		if (node == null) {
			return nodeList;
		}
		
		nodeList.add(node);
		
		if (node.left != null) {
			nodeList.addAll(nodeToList(node.left));
		}
		if (node.right != null) {
			nodeList.addAll(nodeToList(node.right));
		}
		return nodeList;
	}
	
	static void visit(Node node, int height, Map<Integer, List<Node>> map) {
		
		if (node == null) {
			return;
		}
		int currHeight = height + 1;
		if (!map.containsKey(currHeight)) {
			map.put(currHeight, new ArrayList<>());
		}
		map.get(currHeight).add(node);
		if (node.left != null) {
			visit(node.left, currHeight, map);
		}
		if (node.right != null) {
			visit(node.right, currHeight, map);
		}
	}
}
