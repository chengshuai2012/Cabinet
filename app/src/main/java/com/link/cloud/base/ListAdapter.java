package com.link.cloud.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 作者：qianlu on 2018/9/30 16:01
 * 邮箱：zar.l@qq.com
 */
public abstract class ListAdapter<T> extends BaseAdapter implements AbsListView.RecyclerListener {
    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;
    protected OnItemClickListener onItemClickListener;

    public ListAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    public Object getItem(int position) {
        return this.mList != null ? this.mList.get(position) : null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public Object remove(int position) {
        return this.mList != null && !this.mList.isEmpty() ? this.mList.remove(position) : null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract View getView(int var1, View var2, ViewGroup var3);

    public void setList(List<T> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public List<T> getList() {
        return this.mList;
    }

    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList(list.length);
        Object[] var3 = list;
        int var4 = list.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            T t = (T) var3[var5];
            arrayList.add(t);
        }

        this.setList((List) arrayList);
    }

    public void addAll(Collection<T> list) {
        if (list != null) {
            this.mList.addAll(list);
            this.notifyDataSetChanged();
        }

    }

    public ListView getListView() {
        return this.mListView;
    }

    public void setListView(ListView listView) {
        this.mListView = listView;
    }

    public Object getItemAtPosition(int position) {
        return this.mList.get(position);
    }

    public void onMovedToScrapHeap(View view) {
    }

    public Context getContext() {
        return this.mContext;
    }

    public void clear() {
        if (this.mList != null) {
            this.mList.clear();
        }

        this.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public ViewHolder() {
        }

        public static <T extends View> T get(View convertView, int resId) {
            SparseArray<View> viewHolder = (SparseArray) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray();
                convertView.setTag(viewHolder);
            }

            View childView = (View) viewHolder.get(resId);
            if (childView == null) {
                childView = convertView.findViewById(resId);
                viewHolder.put(resId, childView);
            }

            return (T) childView;
        }
    }
}
