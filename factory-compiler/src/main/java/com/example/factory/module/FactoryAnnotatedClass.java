package com.example.factory.module;


import com.zhpan.annotation.annotation.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {
    private TypeElement annotatedClassElement;
    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) {
        this.annotatedClassElement = classElement;
        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();
        if (id.length() == 0) {
            throw new IllegalArgumentException(
                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
                            Factory.class.getSimpleName(), classElement.getQualifiedName().toString()));
        }
        // Get the full QualifiedTypeName
        try {
        Class<?> clazz = annotation.type();
        qualifiedSuperClassName=clazz.getCanonicalName();
        simpleTypeName=clazz.getSimpleName();
        }catch (MirroredTypeException mte){
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }

    public String getId() {
        return id;
    }

    public String getQualifiedFactoryGroupName() {
        return qualifiedSuperClassName;
    }


    public String getSimpleFactoryGroupName() {
        return simpleTypeName;
    }

    public TypeElement getTypeElement() {
        return annotatedClassElement;
    }
}
