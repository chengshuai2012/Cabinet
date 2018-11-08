package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;

import io.realm.RealmList;

/**
 * Created by 49488 on 2018/11/5.
 */

public class VipController {
    VipControllerListener listener;

    public interface VipControllerListener {
        void onVipErrorCode(String msg);

        void onVipFail(Throwable e, boolean isNetWork);

        void getUserSuccess(BindUser data);

        void VipCabinetSuccess(CabinetInfo vipCabinetUser);

    }

    private BaseService api;

    public VipController(VipControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }

    public void getUser(int pageNume, int Page) {
        RequestBindFinger requestBindFinger = new RequestBindFinger();
        requestBindFinger.setContent("CHINA00001");
        requestBindFinger.setPageNo(Page);
        requestBindFinger.setPageSize(pageNume);
        api.getUser(requestBindFinger).compose(IOMainThread.<BaseEntity<BindUser>>composeIO2main()).subscribe(new BaseObserver<BindUser>() {
            @Override
            protected void onSuccees(BaseEntity<BindUser> t) {
                listener.getUserSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                listener.onVipErrorCode(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onVipFail(e, isNetWorkError);
            }
        });

    }

    public void OpenVipCabinet(String finger, String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setFingerprint(finger);
        retrunCabinetRequest.setUuid(uuid);
        api.VIPCabinet(retrunCabinetRequest)
                .compose(IOMainThread.<BaseEntity<CabinetInfo>>composeIO2main())
                .subscribe(new BaseObserver<CabinetInfo>() {
                    @Override
                    protected void onSuccees(BaseEntity<CabinetInfo> t) {
                        listener.VipCabinetSuccess(t.getData());
                    }

                    @Override
                    protected void onCodeError(String msg, String codeErrorr) {
                        listener.onVipErrorCode(msg);
                    }


                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {
                        listener.onVipFail(e, isNetWorkError);
                    }

                });
    }


}
