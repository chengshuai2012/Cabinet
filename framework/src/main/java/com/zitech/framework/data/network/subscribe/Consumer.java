package com.zitech.framework.data.network.subscribe;

import rx.Subscriber;

/**
 * Created by lu on 2017/11/30.
 */

public abstract class Consumer<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(T t) {
        accept(t);
    }

    protected abstract void accept(T t);
}
