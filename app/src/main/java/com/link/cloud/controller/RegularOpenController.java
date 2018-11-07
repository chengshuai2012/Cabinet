package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.RetrunCabinetRequest;

/**
 * 作者：qianlu on 2018/11/7 14:53
 * 邮箱：zar.l@qq.com
 */
public class RegularOpenController {


    RegularOpenController.RegularOpenControllerListener listener;
    private BaseService api;

    public interface RegularOpenControllerListener {

        void OpenSuccess(CabinetInfo cabinetInfo);

        void returnSuccess(CabinetInfo cabinetInfo);

        void openFaild(String message);

        void returnFail(String message);
    }


    public RegularOpenController(RegularOpenControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }


    public void returnCabinet(String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setUuid(uuid);
        api.returnCabinet(retrunCabinetRequest)
                .compose(IOMainThread.<BaseEntity<CabinetInfo>>composeIO2main())
                .subscribe(new BaseObserver<CabinetInfo>() {

                    @Override
                    protected void onSuccees(BaseEntity<CabinetInfo> t) {
                        listener.returnSuccess(t.getData());
                    }

                    @Override
                    protected void onCodeError(String msg, String codeErrorr) {
                        listener.returnFail(msg);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {

                    }
                });
    }


    public void temCabinet(final String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setUuid(uuid);

        api.temCabinet(retrunCabinetRequest).compose(IOMainThread.<BaseEntity<CabinetInfo>>composeIO2main()).subscribe(new BaseObserver<CabinetInfo>() {

            @Override
            protected void onSuccees(BaseEntity<CabinetInfo> t) {
                listener.OpenSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                    listener.openFaild(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.returnFail(e.getMessage());
            }
        });

    }


}
