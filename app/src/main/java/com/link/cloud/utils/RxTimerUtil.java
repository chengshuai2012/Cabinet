package com.link.cloud.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 作者：qianlu on 2018/10/29 16:28
 * 邮箱：zar.l@qq.com
 */
public class RxTimerUtil {


    private static Subscription mDisposable;

    /**
     * milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public void timer(long milliseconds, final IRxNext next) {
        mDisposable = Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancel();
                    }
                });
    }


    /**
     * 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public void interval(final long milliseconds, final IRxNext next) {
        mDisposable = Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("onCompleted: ","0000" );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("onCompleted: ",e.getMessage());
                        interval(milliseconds, next);
                    }

                });
    }


    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null) {
            mDisposable.unsubscribe();
        }
        Log.e( "cancel: " ,"1111111111");
    }

    public interface IRxNext {
        void doNext(long number);
    }
}
