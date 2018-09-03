package com.zhpan.mannotation.factory;


import com.zhpan.annotation.annotation.Factory;

@Factory(id = "Rectangle", type = Shape.class)
public class Rectangle implements Shape {
	@Override
	public void draw() {
		System.out.println("Draw a Rectangle");
	}
}
