package com.example.factory.module;


import com.zhpan.annotation.annotation.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {
    private TypeElement mAnnotatedClassElement;
    private String mQualifiedSuperClassName;
    private String mSimpleTypeName;
    private String mId;

    public FactoryAnnotatedClass(TypeElement classElement) {
        this.mAnnotatedClassElement = classElement;
        Factory annotation = classElement.getAnnotation(Factory.class);
        mId = annotation.id();
        if (mId.length() == 0) {
            throw new IllegalArgumentException(
                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
                            Factory.class.getSimpleName(), classElement.getQualifiedName().toString()));
        }
        // Get the full QualifiedTypeName
        try {
            Class<?> clazz = annotation.type();
            mQualifiedSuperClassName = clazz.getCanonicalName();
            mSimpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            mQualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            mSimpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }

    public String getId() {
        return mId;
    }

    public String getQualifiedFactoryGroupName() {
        return mQualifiedSuperClassName;
    }


    public String getSimpleFactoryGroupName() {
        return mSimpleTypeName;
    }

    public TypeElement getTypeElement() {
        return mAnnotatedClassElement;
    }
}
