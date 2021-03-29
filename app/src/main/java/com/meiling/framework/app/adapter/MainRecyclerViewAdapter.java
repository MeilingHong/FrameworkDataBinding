package com.meiling.framework.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.meiling.framework.R;
import com.meiling.framework.app.adapter.base.DataBindRecyclerViewCallback;
import com.meiling.framework.app.adapter.base.DataBindingRecyclerViewAdapter;
import com.meiling.framework.app.adapter.base.DataBindingRecyclerViewHolder;
import com.meiling.framework.app.viewmodel.recycler.RecyclerEntity;
import com.meiling.framework.databinding.ItemRecyclerview1Binding;
import com.meiling.framework.databinding.ItemRecyclerview2Binding;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerViewAdapter extends DataBindingRecyclerViewAdapter<RecyclerEntity> {

    public MainRecyclerViewAdapter(Context mContext, List<RecyclerEntity> mList, DataBindRecyclerViewCallback mCallback) {
        super(mContext, mList, mCallback);
    }

    /**
     * todo 考虑到当有多个布局需要进行实例化时，不好进行通用的对应，所以onCreateViewHolder方法的实例化还是需要在子类中进行实现
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            // todo 需要考虑下，在需要多个Item布局时，怎么处理，Holder实例化的抽象问题；
            //  【暂时没考虑到怎么保证单个item布局与多个item布局统一，由于不同item布局本身在databinding时，
            //  会生成不同的databinding对象，在Holder泛型化后，似乎没有好的办法针对这个部分进行进一步的抽象封装】
            //  ---对象可以进行泛型的调用，但静态类无法使用这个特性
            return new DataBindingRecyclerViewHolder<ItemRecyclerview1Binding>(initItemView(R.layout.item_recyclerview1, parent));
        } else {
            return new DataBindingRecyclerViewHolder<ItemRecyclerview2Binding>(initItemView(R.layout.item_recyclerview2, parent));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position % 2 == 0) {// view Type 0
            DataBindingRecyclerViewHolder<ItemRecyclerview1Binding> temp = (DataBindingRecyclerViewHolder<ItemRecyclerview1Binding>) holder;
            temp.itemLayoutBinding.setNameEntity(mList != null && mList.size() > position ? mList.get(position) : new RecyclerEntity());
            temp.itemLayoutBinding.itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCallback(R.id.itemName, 0, holder, position);
                }
            });
        } else {
            DataBindingRecyclerViewHolder<ItemRecyclerview2Binding> temp = (DataBindingRecyclerViewHolder<ItemRecyclerview2Binding>) holder;
            temp.itemLayoutBinding.setNameEntity(mList != null && mList.size() > position ? mList.get(position) : new RecyclerEntity());
            temp.itemLayoutBinding.itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCallback(R.id.itemName, 1, holder, position);
                }
            });
            temp.itemLayoutBinding.itemAgeCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCallback(R.id.itemAgeCover, 1, holder, position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

}
