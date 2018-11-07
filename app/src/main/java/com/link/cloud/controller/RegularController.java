package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.RetrunCabinetRequest;

/**
 * 作者：qianlu on 2018/11/7 11:10
 * 邮箱：zar.l@qq.com
 */
public class RegularController {

    RegularController.RegularControllerListener regularControllerListener;
    private BaseService api;


    public interface RegularControllerListener {

        void successful(AllUser allUser);

        void faild(String message);

        void onRegularFail(Throwable e, boolean isNetWork);
    }


    public RegularController(RegularControllerListener regularControllerListener) {
        api = RetrofitFactory.getInstence().API();
        this.regularControllerListener = regularControllerListener;
    }


    public void findUser(String finger) {
        RetrunCabinetRequest request=new RetrunCabinetRequest();
        request.setFingerprint(finger);
        api.findUser(request).compose(IOMainThread.<BaseEntity>composeIO2main()).subscribe(new BaseObserver() {
            @Override
            protected void onSuccees(BaseEntity t) {
//                regularControllerListener.successful(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                regularControllerListener.faild(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                regularControllerListener.onRegularFail(e, isNetWorkError);
            }
        });

    }

}
