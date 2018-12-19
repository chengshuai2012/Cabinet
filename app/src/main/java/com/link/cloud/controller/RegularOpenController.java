package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.APPVersionBean;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.QrRequest;
import com.link.cloud.network.bean.RetrunCabinetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

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

        void openFaild(String message,String code);

        void returnFail(String message,String code);

       void  onFail(Throwable e,boolean isNetWork);

       void SuccessByQr(CabinetInfo cabinetInfo);

        void OpenLockByPass(APPVersionBean appVersionBean);
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
                        listener.returnFail(msg,codeErrorr);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {
                        listener.onFail(e,isNetWorkError);
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
                    listener.openFaild(msg,codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onFail(e,isNetWorkError);
            }
        });

    }
    public void OpenLockByPass(String uuid,String passWord) {
        api.validate(uuid,passWord).compose(IOMainThread.<BaseEntity<APPVersionBean>>composeIO2main()).subscribe(new BaseObserver<APPVersionBean>() {


            @Override
            protected void onSuccees(BaseEntity<APPVersionBean> t) {
                listener.OpenLockByPass(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                listener.openFaild(msg,codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onFail(e, isNetWorkError);
            }
        });

    }

}
