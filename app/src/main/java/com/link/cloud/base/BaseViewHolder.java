package com.link.cloud.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by qianlu on 2018/5/16.
 * 邮箱：zar.l@qq.com
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(Object data, int position);

        void onCheackClick(int position);
    }


    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(Activity activity, T data, int position);

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}
