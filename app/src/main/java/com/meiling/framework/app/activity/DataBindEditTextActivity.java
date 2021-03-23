package com.meiling.framework.app.activity;

import android.os.Bundle;
import android.view.View;

import com.meiling.framework.R;
import com.meiling.framework.app.viewmodel.data.Data;
import com.meiling.framework.base.BaseActivity;
import com.meiling.framework.databinding.ActivityDataBindEdittextBinding;
import com.meiling.framework.utils.log.Ulog;

import java.util.Random;

public class DataBindEditTextActivity extends BaseActivity<ActivityDataBindEdittextBinding> {
    private Data data;

    @Override
    public void setConfiguration() {

    }

    @Override
    public int layoutViewId() {
        return R.layout.activity_data_bind_edittext;
    }

    @Override
    public void afterDestroy() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void lazyLoadCallback() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo 这里相当于初始化一个ViewModel，并注入到绑定对象中，当操作这个ViewModel时，关联的View跟着一起改变
        data = new Data();
        data.setName("自定义（DataBindTextView）");
        layoutBinding.setNameEntity(data);


        layoutBinding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setName(new Random().nextInt() + "--随机修改绑定的Data中的值");
                Ulog.i(data.toString());
            }
        });
    }

}