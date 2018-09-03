package com.zhpan.mannotation.factory;

import com.zhpan.annotation.annotation.Factory;

@Factory(type = Shape.class, id = "TestShape")
public class TestShape implements Shape {
    @Override
    public void draw() {

    }
}
