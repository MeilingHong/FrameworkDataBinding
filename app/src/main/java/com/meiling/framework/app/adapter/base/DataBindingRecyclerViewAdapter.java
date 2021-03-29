package com.meiling.framework.app.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
 * 这种实现存在一个问题：限定死了DataBinding的布局只能使用一次，导致不好进行扩展【<T, P extends ViewDataBinding>】
 * 所以，最好还是在子类中在实际中进行实现时
 */
public abstract class DataBindingRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mList = new ArrayList<>();
    protected Context mContext;
    protected DataBindRecyclerViewCallback mCallback;

    public DataBindingRecyclerViewAdapter(Context mContext, List<T> mList, DataBindRecyclerViewCallback mCallback) {
        if (mContext == null) {
            throw new IllegalArgumentException("非法Adapter参数（Context）");
        }
        if (mList == null) {// 避免传入空数组导致的空指针问题
            mList = new ArrayList<>();
        }
        this.mList = mList;
        this.mContext = mContext.getApplicationContext();// 避免传入的Context销毁导致的空指针问题，使用ApplicationContext
        this.mCallback = mCallback;
    }

    public View initItemView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    public void doCallback(int viewId, int viewType, RecyclerView.ViewHolder holder, int position) {
        if (mCallback != null) {
            mCallback.onViewClickCallback(viewId, viewType, holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
