package com.zhpan.compiler.exception;

import javax.lang.model.element.Element;

public class ProcessingException extends Exception{

    public ProcessingException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
