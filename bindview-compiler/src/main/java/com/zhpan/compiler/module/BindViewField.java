package com.zhpan.compiler.module;

import com.zhpan.annotation.annotation.BindView;
import com.zhpan.compiler.exception.ProcessingException;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class BindViewField {
    private VariableElement mFieldElement;

    private int mResId;

    public BindViewField(Element element) throws ProcessingException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new ProcessingException("Only field can be annotated with @%s", BindView.class.getSimpleName());
        }
        mFieldElement = (VariableElement) element;

        BindView bindView = mFieldElement.getAnnotation(BindView.class);
        mResId = bindView.value();
        if (mResId < 0) {
            throw new ProcessingException("value() in %s for field % is not valid", BindView.class.getSimpleName(), mFieldElement.getSimpleName());
        }
    }

    public VariableElement getFieldElement() {
        return mFieldElement;
    }

    public int getResId() {
        return mResId;
    }

    public Name getFieldName(){
        return mFieldElement.getSimpleName();
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}
