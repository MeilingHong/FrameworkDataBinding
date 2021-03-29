package com.meiling.framework.app.adapter.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 针对Holder的泛型化，避免针对不同的Item布局实例化，需要进行额外的重复代码编写
 * @param <T>
 */
public class DataBindingRecyclerViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public T itemLayoutBinding;

    public DataBindingRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        itemLayoutBinding = DataBindingUtil.bind(itemView);
    }
}
