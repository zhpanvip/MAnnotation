package com.zhpan.api;

import com.zhpan.api.finder.Finder;

public interface Injector<T> {
    void inject(T target,Object source,Finder finder);
}
