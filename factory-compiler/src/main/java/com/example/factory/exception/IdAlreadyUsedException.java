package com.example.factory.exception;

import com.example.factory.module.FactoryAnnotatedClass;

public class IdAlreadyUsedException extends RuntimeException {
    public IdAlreadyUsedException(FactoryAnnotatedClass factoryAnnotatedClass) {

    }
}
