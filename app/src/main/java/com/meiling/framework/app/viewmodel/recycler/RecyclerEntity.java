package com.meiling.framework.app.viewmodel.recycler;
/**
 * Created by marisareimu@126.com on 2021-03-11  15:56
 * project DataBinding
 */

import com.meiling.framework.BR;
import com.meiling.framework.app.viewmodel.ToString;

import java.io.Serializable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * todo 相对而言该方式限制较大
 * <p>
 * Created by huangzhou@ulord.net on 2021-03-11  15:56
 * project DataBinding
 */
public class RecyclerEntity extends ToString implements Serializable {
    @Bindable
    private String name;
    @Bindable
    public int age;

    public RecyclerEntity() {
    }

    public RecyclerEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);//
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);//
    }
}
