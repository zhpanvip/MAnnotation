package com.zhpan.compiler.module;

import com.zhpan.annotation.annotation.OnClick;
import com.zhpan.compiler.exception.ProcessingException;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.swing.text.View;
import javax.tools.Diagnostic;

public class OnClickMethod {
    private Messager mMessager;
    private Name mMethodName;
    private int[] ids;

    public OnClickMethod(Element element, Messager messager) throws ProcessingException {
        this.mMessager = messager;
        if (element.getKind() != ElementKind.METHOD) {
            throw new ProcessingException("Only method can be annotated width @%s", OnClick.class.getSimpleName());
        }
        ExecutableElement methodElement = (ExecutableElement) element;
        mMethodName = methodElement.getSimpleName();
        OnClick onClick = methodElement.getAnnotation(OnClick.class);
        ids = onClick.value();
        if (ids.length <= 0) {
            throw new IllegalArgumentException(String.format("Must set valid ids for @%s", OnClick.class.getSimpleName()));
        } else {
            for (int id : ids) {
                if (id < 0) {
                    throw new IllegalArgumentException(String.format("Must set valid ids for @%s", OnClick.class.getSimpleName()));
                }
            }
        }
        List<? extends VariableElement> parameters = methodElement.getParameters();
        if (parameters.size() != 1) {
            throw new IllegalArgumentException(String.format("The method annotated with @%s must have a parameter", OnClick.class.getSimpleName()));
        }
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    public Name getMethodName() {
        return mMethodName;
    }

    public int[] getIds() {
        return ids;
    }
}
