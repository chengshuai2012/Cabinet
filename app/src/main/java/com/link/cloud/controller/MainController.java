package com.link.cloud.controller;


import android.os.Environment;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.FileDownLoadSubscriber;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.APPVersionBean;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;
import com.zitech.framework.utils.ToastMaster;

import java.io.File;

import io.realm.RealmList;

import static com.link.cloud.network.IOMainThread.ioMainDownload;

/**
 * Created by 49488 on 2018/10/28.
 */

public class MainController {
    MainControllerListener listener;

    public interface MainControllerListener {
        void onLoginSuccess(CabnetDeviceInfoBean cabnetDeviceInfoBean);

        void onMainErrorCode(String msg);

        void onMainFail(Throwable e, boolean isNetWork);

        void getUserSuccess(BindUser data);

        void onCabinetInfoSuccess(RealmList<CabinetInfo> data);

        void getVersionSuccess(APPVersionBean appVersionBean);

        void temCabinetSuccess(CabinetInfo cabinetBean);

    }

    private BaseService api;

    public MainController(MainControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }

    public void login(String userNmae, String password) {
        api.appLogin(userNmae, password)
                .compose(IOMainThread.<BaseEntity<CabnetDeviceInfoBean>>composeIO2main())
                .subscribe(new BaseObserver<CabnetDeviceInfoBean>() {
                    @Override
                    protected void onSuccees(BaseEntity<CabnetDeviceInfoBean> t)  {
                        listener.onLoginSuccess(t.getData());
                    }

                    @Override
                    protected void onCodeError(String msg,String codeErrorr) {
                        listener.onMainErrorCode(codeErrorr);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError)  {
                        listener.onMainFail(e, isNetWorkError);
                    }
                });
    }

    public void getUser(int pageNume, int Page) {
        RequestBindFinger requestBindFinger = new RequestBindFinger();
        requestBindFinger.setContent("CHINA00001");
        requestBindFinger.setPageNo(Page);
        requestBindFinger.setPageSize(pageNume);
        api.getUser(requestBindFinger).compose(IOMainThread.<BaseEntity<BindUser>>composeIO2main()).subscribe(new BaseObserver<BindUser>() {
            @Override
            protected void onSuccees(BaseEntity<BindUser> t)  {
                listener.getUserSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg,String codeErrorr)  {
                ToastMaster.shortToast(msg);
                listener.onMainErrorCode(msg);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onMainFail(e, isNetWorkError);
            }
        });

    }

    public void returnCabinet(String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setUuid(uuid);
        api.returnCabinet(retrunCabinetRequest)
                .compose(IOMainThread.<BaseEntity<CabinetInfo>>composeIO2main())
                .subscribe(new BaseObserver<CabinetInfo>() {

                    @Override
                    protected void onSuccees(BaseEntity<CabinetInfo> t) {
                        ToastMaster.shortToast(t.getSecondMessage());
                    }

                    @Override
                    protected void onCodeError(String msg,String codeErrorr) {
                        ToastMaster.shortToast(msg);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) {
                        ToastMaster.shortToast(e.getMessage());
                    }
                });
    }

    public void temCabinet(final String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setUuid(uuid);

        api.temCabinet(retrunCabinetRequest).compose(IOMainThread.<BaseEntity<CabinetInfo>>composeIO2main()).subscribe(new BaseObserver<CabinetInfo>() {

            @Override
            protected void onSuccees(BaseEntity<CabinetInfo> t) {
                listener.temCabinetSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg,String codeErrorr)  {
              ToastMaster.shortToast(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError)  {
                listener.onMainFail(e, isNetWorkError);
            }
        });

    }

    public void useCabinet(String uuid) {
        RetrunCabinetRequest retrunCabinetRequest = new RetrunCabinetRequest();
        retrunCabinetRequest.setUuid(uuid);
        api.useCabinet(retrunCabinetRequest)
                .compose(IOMainThread.<BaseEntity>composeIO2main())
                .subscribe(new BaseObserver() {

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    protected void onSuccees(BaseEntity t) {

                    }

                    @Override
                    protected void onCodeError(String msg,String codeErrorr) {

                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError)  {

                    }
                });
    }

    public void getCabinetInfo() {
        api.getCabinetInfo()
                .compose(IOMainThread.<BaseEntity<RealmList<CabinetInfo>>>composeIO2main())
                .subscribe(new BaseObserver<RealmList<CabinetInfo>>() {
                               @Override
                               protected void onSuccees(BaseEntity<RealmList<CabinetInfo>> t)  {
                                   listener.onCabinetInfoSuccess(t.getData());
                               }

                               @Override
                               protected void onCodeError(String msg,String codeErrorr) {

                               }

                               @Override
                               protected void onFailure(Throwable e, boolean isNetWorkError) {
                                   listener.onMainFail(e, isNetWorkError);
                               }
                           }
                );
    }
    public void getAppVersion(){
        api.getAppVersion(2).compose(IOMainThread.<BaseEntity<APPVersionBean>>composeIO2main()).subscribe(new BaseObserver<APPVersionBean>() {

            @Override
            protected void onSuccees(BaseEntity<APPVersionBean> t) {
                listener.getVersionSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                listener.onMainErrorCode(codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onMainFail(e, isNetWorkError);
            }
        });
    }
    String TAG ="download";
    public void downloadFile() {
        File file = new File(Environment.getExternalStorageDirectory()+"/lingxi.apk");
        api.getApp(2).
                compose(ioMainDownload()).
                subscribeWith(new FileDownLoadSubscriber(file){
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                });

    }
}
