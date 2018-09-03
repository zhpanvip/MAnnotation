package com.zhpan.api.finder;

import android.content.Context;
import android.view.View;

public interface Finder {
    Context getContext(Object source);

    View findView(Object source,int id);
}
