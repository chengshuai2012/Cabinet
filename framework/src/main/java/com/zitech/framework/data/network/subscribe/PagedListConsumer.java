package com.zitech.framework.data.network.subscribe;

import com.zitech.framework.data.network.exception.ApiException;
import com.zitech.framework.utils.ToastMaster;

/**
 * Created by lu on 2017/11/30.
 */

public abstract class PagedListConsumer<T> extends Consumer<T> {


    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof ApiException) {
            ToastMaster.shortToast(throwable.getMessage());
        }
    }

}
