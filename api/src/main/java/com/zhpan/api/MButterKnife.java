package com.zhpan.api;

import android.app.Activity;
import android.view.View;

import com.zhpan.api.finder.ActivityFinder;
import com.zhpan.api.finder.Finder;

import java.util.HashMap;
import java.util.Map;

public class MButterKnife {

    private static final ActivityFinder sActivityFinder = new ActivityFinder();

    private static Map<String, Injector> sFinderMap = new HashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, sActivityFinder);
    }

 /*   public static void bind(Object target,View view){
        bind(target,view,sActivityFinder);
    }*/


    public static void bind(Object target, Object source, Finder finder) {
        String name = target.getClass().getName();
        try {
            Injector injector = sFinderMap.get(name);
            if (injector == null) {
                Class<?> finderClass = Class.forName(name + "_Injector");
                injector = (Injector) finderClass.newInstance();
                sFinderMap.put(name,injector);
            }
            injector.inject(target,source,finder);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
