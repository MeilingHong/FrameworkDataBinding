package com.meiling.framework.utils.rxjava;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.meiling.framework.utils.log.Ulog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 异步任务替代工具类
 */

public class RxJavaUtil {
    private CompositeDisposable compositeDisposable;
    private static final RxJavaUtil ourInstance = new RxJavaUtil();

    public static RxJavaUtil getInstance() {
        return ourInstance;
    }

    private RxJavaUtil() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    public void release() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void addDisposable(Observable<?> observable, DisposableObserver observer) {
        // Schedulers【IoScheduler】 实例本身使用的是，线程池的那一套，不过是根据自己的需要进行线程池对应的参数配置
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                // mainThread 本身基于handler来进行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }

    public void doExample(Context context,String stringExample) {
        //        // 例子：如果需要进行转换
        Observable observable = Observable.just(stringExample).
                flatMap(new Function<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(String inputParameter) throws Exception {
                        // todo 将just传入的对象，进行相应的处理，判断，当满足条件时，转换成Integer类型，传入下一个步骤，
                        //  当出错时抛出异常
                        //  正常处理返回的Integer类型的的数据将返回到observer中对应的Next方法中
                        if (!TextUtils.isEmpty(inputParameter) && "A".equals(inputParameter)) {
                            return Observable.just(1);
                        } else {
                            return Observable.error(new Exception("IllegalArguement"));
                        }
                    }
                });

        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer result) {
                Toast.makeText(context, result!=null?result.toString():"正常，但数据为空", Toast.LENGTH_SHORT).show();
                Ulog.e(result!=null?result.toString():"正常，但数据为空");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, e!=null?e.getMessage():"异常！", Toast.LENGTH_SHORT).show();
                Ulog.e(e!=null?e.getMessage():"异常！");
            }

            @Override
            public void onComplete() {

            }
        };

        /*
         * 1、处理方式一
         */
//        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(observer);
        /*
         * 2、处理方式二
         */
        addDisposable(observable,observer);
    }
}
