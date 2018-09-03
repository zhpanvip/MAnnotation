package com.zhpan.mannotation.factory;


import com.zhpan.annotation.annotation.Factory;

@Factory(id = "Triangle", type = Shape.class)
public class Triangle implements Shape {
	@Override
	public void draw() {
		System.out.println("Draw a Triangle");
	}
}
