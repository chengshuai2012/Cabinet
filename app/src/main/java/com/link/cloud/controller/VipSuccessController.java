package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.EdituserRequest;
import com.link.cloud.network.bean.PassWordValidate;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;

import okhttp3.RequestBody;

/**
 * Created by 49488 on 2018/11/5.
 */

public class VipSuccessController {
    VipControllerListener listener;

    public interface VipControllerListener {
        void onVipErrorCode(String msg);

        void onVipFail(Throwable e, boolean isNetWork);

        void OpenLockByPass(PassWordValidate appVersionBean);
    }

    private BaseService api;

    public VipSuccessController(VipControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }

    public void OpenLockByPass(String uuid,String passWord) {
        api.validate(uuid,passWord).compose(IOMainThread.<BaseEntity<PassWordValidate>>composeIO2main()).subscribe(new BaseObserver<PassWordValidate>() {


            @Override
            protected void onSuccees(BaseEntity<PassWordValidate> t) {
                listener.OpenLockByPass(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                listener.onVipErrorCode(codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onVipFail(e, isNetWorkError);
            }
        });

    }

}
