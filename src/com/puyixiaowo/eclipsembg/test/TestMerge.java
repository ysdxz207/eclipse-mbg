package com.puyixiaowo.eclipsembg.test;

public class TestMerge {
	public static void main(String[] args) {
		A a = new A();
		A b = new A();
		a.setX("哈哈");
		b.setY("嘿嘿");
		
		
		System.out.println(b.getX());
		System.out.println(b.getY());
		
	}
}
