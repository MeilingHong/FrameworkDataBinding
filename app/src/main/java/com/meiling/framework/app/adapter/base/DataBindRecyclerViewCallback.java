package com.meiling.framework.app.adapter.base;


import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public interface DataBindRecyclerViewCallback {
    /**
     *
     * @param viewId
     * @param viewType
     * @param holder todo 考虑到多个布局的兼容的问题，直接返回holder对象，而不返回特定的Holder
     *                也可以直接返回View，根据实际的需求来确定该View的类型
     * @param position
     */
    void onViewClickCallback(int viewId, int viewType, RecyclerView.ViewHolder holder, int position);
}
