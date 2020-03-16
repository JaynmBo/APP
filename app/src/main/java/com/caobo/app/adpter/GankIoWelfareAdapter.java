package com.caobo.app.adpter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caobo.app.R;
import com.caobo.app.model.FileBean;
import com.caobo.app.utils.DateUtils;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public class GankIoWelfareAdapter extends BaseCompatAdapter<FileBean, BaseViewHolder>{
    public GankIoWelfareAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        Glide.with(mContext)
                .load(item.getUrl())
                .crossFade(500)
                .placeholder(R.mipmap.img_default_meizi)
                .into((ImageView) helper.getView(R.id.iv_item_image));
        helper.setText(R.id.tv_Time, DateUtils.timeStamp2Date(item.getTime(),null));
    }

    public GankIoWelfareAdapter(@LayoutRes int layoutResId, @Nullable List<FileBean>
            data) {
        super(layoutResId, data);
    }

    public GankIoWelfareAdapter(@Nullable List<FileBean> data) {
        super(data);
    }

}
