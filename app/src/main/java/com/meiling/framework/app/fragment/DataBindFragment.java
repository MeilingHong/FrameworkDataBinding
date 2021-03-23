package com.meiling.framework.app.fragment;

import com.meiling.framework.R;
import com.meiling.framework.app.viewmodel.data.Data;
import com.meiling.framework.base.BaseFragment;
import com.meiling.framework.databinding.FragmentDataBindBinding;

public class DataBindFragment extends BaseFragment<FragmentDataBindBinding> {

    @Override
    public int layoutViewId() {
        return R.layout.fragment_data_bind;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {
        Data data = new Data();
        data.setName("Fragment");
        layoutFragmentBinding.setNameEntity(data);
    }

    @Override
    public void lazyLoadCallback() {

    }
}
