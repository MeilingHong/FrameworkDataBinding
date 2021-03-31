package com.meiling.framework.app.fragment;

import android.view.View;

import com.meiling.framework.R;
import com.meiling.framework.app.dialog.loading.LoadingDialog;
import com.meiling.framework.app.viewmodel.data.Data;
import com.meiling.framework.base.BaseFragment;
import com.meiling.framework.databinding.FragmentDataBindBinding;
import com.meiling.framework.dialog.callback.IDialogDismissCallback;

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
        layoutFragmentBinding.setClickPresenter(new FragmentClickPresenter());
    }

    @Override
    public void lazyLoadCallback() {

    }

    private LoadingDialog loadingDialog;

    public class FragmentClickPresenter {
        public void showDialog(View view) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog();
                loadingDialog.setDialogConfig(getContext(), null, new IDialogDismissCallback() {
                    @Override
                    public void afterDialogDismiss() {
                        if (loadingDialog != null) {
                            loadingDialog = null;
                        }
                    }
                });
                loadingDialog.show(getFragmentManager(), "loading", 2000);
            }
        }
    }
}
