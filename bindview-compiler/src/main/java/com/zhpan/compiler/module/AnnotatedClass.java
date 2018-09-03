package com.zhpan.compiler.module;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {
    public static final ClassName INJECTOR = ClassName.get("com.zhpan.api", "Injector");
    public static final ClassName FINDER = ClassName.get("com.zhpan.api.finder", "Finder");
    public static final ClassName ONCLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");

    //  Class Name
    public TypeElement mAnnotatedClassElement;
    //  Member was annotated by BindView
    public List<BindViewField> mFieldList;
    //  method was annotated by OnClick
    public List<OnClickMethod> mMethodList;

    public Elements mElementsUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementsUtils) {
        this.mAnnotatedClassElement = classElement;
        this.mElementsUtils = elementsUtils;
        mFieldList = new ArrayList<>();
        mMethodList = new ArrayList<>();
    }


    public JavaFile generateCode() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mAnnotatedClassElement.asType()), "target", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(FINDER, "finder");

        for (BindViewField field : mFieldList) {
            methodBuilder.addStatement("target.$N=($T)finder.findView(source,$L)", field.getFieldName(),
                    ClassName.get(field.getFieldType()), field.getResId());
        }

        if (mMethodList.size() > 0) {

            for (OnClickMethod method : mMethodList) {
                MethodSpec build = MethodSpec.methodBuilder("onClick")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ANDROID_VIEW, "view")
                        .addStatement("target.$N(view)", method.getMethodName()).build();

                TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ONCLICK_LISTENER)
                        .addMethod(build)
                        .build();
                methodBuilder.addStatement("$T listener = $L", ONCLICK_LISTENER, listener);

                for (int id : method.getIds()) {
                    methodBuilder.addStatement("finder.findView(source,$L).setOnClickListener(listener)", id);
                }
            }
        }


        String packageName = getPackageName(mAnnotatedClassElement);
        String className = getClassName(mAnnotatedClassElement, packageName);
        ClassName bindClassName = ClassName.get(packageName, className);

        TypeSpec finderClass = TypeSpec.classBuilder(bindClassName.simpleName() + "_Injector")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(INJECTOR, TypeName.get(mAnnotatedClassElement.asType())))
                .addMethod(methodBuilder.build())
                .build();
        return JavaFile.builder(packageName, finderClass).build();
    }

    private String getClassName(TypeElement annotatedClassElement, String packageName) {
        int packageLen = packageName.length() + 1;
        return annotatedClassElement.getQualifiedName().toString().substring(packageLen).replace('.', '_');
    }

    private String getPackageName(TypeElement annotatedClassElement) {
        return mElementsUtils.getPackageOf(annotatedClassElement).toString();
    }


    public String getQualifiedName() {
        return mAnnotatedClassElement.getQualifiedName().toString();
    }

    public void addField(BindViewField field) {
        mFieldList.add(field);
    }

    public void addMethod(OnClickMethod method) {
        mMethodList.add(method);
    }

}
