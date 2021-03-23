package com.meiling.framework.app.activity;

import android.graphics.Color;
import android.view.View;

import com.meiling.framework.R;
import com.meiling.framework.app.fragment.DataBindFragment;
import com.meiling.framework.base.BaseActivity;
import com.meiling.framework.databinding.ActivityMainBinding;
import com.meiling.framework.utils.log.Ulog;

import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public void setConfiguration() {
        isFullScreen = true;
        isDarkNavigationBarButton = false;
        navigationBarColor = Color.parseColor("#333296fa");
    }

    @Override
    public int layoutViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {
        layoutBinding.setClickPresenter(new MainClickPresenter());
    }

    @Override
    public void lazyLoadCallback() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(String.valueOf(0)) != null) {
//                mainFragment = (DataBindFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(0));
//                transaction.replace(R.id.container, mainFragment, String.valueOf(0));
//                transaction.commit();
            Ulog.w("不执行---重复添加");
        } else {
            mainFragment = new DataBindFragment();
            transaction.add(R.id.container, mainFragment, String.valueOf(0));
            transaction.commit();
            Ulog.w("执行---添加");
        }
    }

    private DataBindFragment mainFragment;

    public class MainClickPresenter {
        /**
         * todo 回调的方法可见性最好使用public，默认的方法会使得布局文件无法访问到对应的方法
         *
         * @param view
         */
        public void jumpToActivity(View view) {
//            startActivity(new Intent(getApplicationContext(), DataBindButtonActivity.class));
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            if (getSupportFragmentManager().findFragmentByTag(String.valueOf(0)) != null) {
////                mainFragment = (DataBindFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(0));
////                transaction.replace(R.id.container, mainFragment, String.valueOf(0));
////                transaction.commit();
//                Ulog.w("不执行---重复添加");
//            } else {
//                mainFragment = new DataBindFragment();
//                transaction.add(R.id.container, mainFragment, String.valueOf(0));
//                transaction.commit();
//                Ulog.w("执行---添加");
//            }
            Ulog.w("执行---点击回调");
        }
    }
}