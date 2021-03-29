package com.meiling.framework.app.activity;

import com.meiling.framework.R;
import com.meiling.framework.app.adapter.MainRecyclerViewAdapter;
import com.meiling.framework.app.adapter.base.DataBindRecyclerViewCallback;
import com.meiling.framework.app.adapter.base.DataBindingRecyclerViewHolder;
import com.meiling.framework.app.viewmodel.recycler.RecyclerEntity;
import com.meiling.framework.base.BaseActivity;
import com.meiling.framework.databinding.ActivityDataBindRecyclerviewBinding;
import com.meiling.framework.databinding.ItemRecyclerview1Binding;
import com.meiling.framework.databinding.ItemRecyclerview2Binding;
import com.meiling.framework.utils.log.Ulog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DataBindRecyclerViewActivity extends BaseActivity<ActivityDataBindRecyclerviewBinding> {

    private MainRecyclerViewAdapter mAdapter;

    @Override
    public void setConfiguration() {

    }

    @Override
    public int layoutViewId() {
        return R.layout.activity_data_bind_recyclerview;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {
        layoutBinding.rvRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<RecyclerEntity> mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(new RecyclerEntity("名称：" + i, i));
        }
        mAdapter = new MainRecyclerViewAdapter(getApplicationContext(), mList, new DataBindRecyclerViewCallback() {
            @Override
            public void onViewClickCallback(int viewId, int viewType, RecyclerView.ViewHolder holder, int position) {
                /**
                 * todo 放在显示页面的回调中处理实际的业务操作，而Adapter中仅关注View间的交互问题【建立关联的地方执行实际业务流程】
                 */
                if (viewType == 0) {
                    DataBindingRecyclerViewHolder<ItemRecyclerview1Binding> temp = (DataBindingRecyclerViewHolder<ItemRecyclerview1Binding>) holder;
                    if (viewId == R.id.itemName) {
                        temp.itemLayoutBinding.getNameEntity().setName(new Random().nextDouble() + "_Click");
                    }
                } else {
                    DataBindingRecyclerViewHolder<ItemRecyclerview2Binding> temp = (DataBindingRecyclerViewHolder<ItemRecyclerview2Binding>) holder;
                    if (viewId == R.id.itemName) {
                        temp.itemLayoutBinding.getNameEntity().setName(new Random().nextLong() + "_Click2");
                    } else if (viewId == R.id.itemAgeCover) {
                        if (temp.itemLayoutBinding.getNameEntity().getAge() < 100000) {
                            temp.itemLayoutBinding.getNameEntity().setAge(temp.itemLayoutBinding.getNameEntity().getAge() + 1);
                        } else {
                            temp.itemLayoutBinding.getNameEntity().setAge(temp.itemLayoutBinding.getNameEntity().getAge() - 1);
                        }
                    }
                }
                Ulog.w("Posotion:" + position + "---" + mList.get(position).toString());// todo 针对DataBinding的Data进行的操作，List中的对应的数据也发生了变化
            }
        });
        layoutBinding.rvRecycler.setAdapter(mAdapter);
    }

    @Override
    public void lazyLoadCallback() {

    }

}