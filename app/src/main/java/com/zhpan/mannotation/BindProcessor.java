package com.zhpan.mannotation;

import android.app.Activity;
import android.view.View;


import com.zhpan.annotation.annotation.BindView;
import com.zhpan.annotation.annotation.InjectLayout;
import com.zhpan.annotation.annotation.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * create by zhpan on 2018/7/24 15:15
 * Describe:
 */
public class BindProcessor {
    public static void bind(Activity activity) {
        injectLayout(activity);
        bindOnClick(activity);
        bindView(activity);
    }

    private static void injectLayout(Activity activity) {
        Class<?> activityClass = activity.getClass();
        if (activityClass.isAnnotationPresent(InjectLayout.class)) {
            InjectLayout mId = activityClass.getAnnotation(InjectLayout.class);
            int id = mId.value();
            try {
                Method method = activityClass.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, id);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bindView(Activity activity) {
        Class<?> activityClass = activity.getClass();
        Field[] declaredFields = activityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(BindView.class)) {
                BindView mId = field.getAnnotation(BindView.class);
                int id = mId.value();
                try {
                    Method method = activityClass.getMethod("findViewById", int.class);
                    method.setAccessible(true);
                    Object view = method.invoke(activity, id);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void bindOnClick(final Activity activity) {
        Class<?> cls = activity.getClass();
        Method[] methods = cls.getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if (method.isAnnotationPresent(OnClick.class)) {
                OnClick mOnclick = method.getAnnotation(OnClick.class);
                int[] ids = mOnclick.value();
                for (int j = 0; j < ids.length; j++) {
                    final View view = activity.findViewById(ids[j]);
                    if (view == null) continue;
                    view.setOnClickListener(v -> {
                        try {
                            method.setAccessible(true);
                            method.invoke(activity, view);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
