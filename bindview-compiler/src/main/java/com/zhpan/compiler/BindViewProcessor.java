package com.zhpan.compiler;

import com.google.auto.service.AutoService;
import com.zhpan.annotation.annotation.BindView;
import com.zhpan.annotation.annotation.OnClick;
import com.zhpan.compiler.exception.ProcessingException;
import com.zhpan.compiler.module.AnnotatedClass;
import com.zhpan.compiler.module.BindViewField;
import com.zhpan.compiler.module.OnClickMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;
    private Elements mElementUtils1;
    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mElementUtils1 = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class.getCanonicalName());
        annotations.add(OnClick.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            processBindView(roundEnvironment);
            processOnClick(roundEnvironment);
        } catch (ProcessingException e) {
            e.printStackTrace();
            return true;
        }
        try {
            for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
                annotatedClass.generateCode().writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAnnotatedClassMap.clear();
        return true;
    }

    private void processBindView(RoundEnvironment roundEnvironment) throws ProcessingException {
        Set<? extends Element> elementsBindView = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsBindView) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField field = new BindViewField(element);
            annotatedClass.addField(field);
        }

    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement encloseElement = (TypeElement) element.getEnclosingElement();
        String qualifiedName = encloseElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(qualifiedName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(encloseElement, mElementUtils);
            mAnnotatedClassMap.put(qualifiedName, annotatedClass);
        }
        return annotatedClass;
    }

    private void processOnClick(RoundEnvironment roundEnvironment) throws ProcessingException {
        Set<? extends Element> elementsOnClick = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elementsOnClick) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            OnClickMethod method = new OnClickMethod(element,mMessager);
            annotatedClass.addMethod(method);
        }
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
