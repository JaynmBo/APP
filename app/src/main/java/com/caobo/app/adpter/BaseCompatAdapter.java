package com.caobo.app.adpter;


import com.caobo.app.widgets.RvLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public abstract class BaseCompatAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T,
        K> {

    public BaseCompatAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        init();
    }

    public BaseCompatAdapter(@Nullable List<T> data) {
        super(data);
        init();
    }

    public BaseCompatAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
        init();
    }

    private void init(){
        setLoadMoreView(new RvLoadMoreView());
        setEnableLoadMore(true);
        openLoadAnimation();
    }
}
