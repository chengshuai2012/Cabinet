package com.link.cloud.controller;


import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetBean;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;
import com.zitech.framework.utils.ToastMaster;

import io.realm.RealmList;

/**
 * Created by 49488 on 2018/10/28.
 */

public class MainController {
    MainControllerListener listener;

    public interface MainControllerListener {
        void onLoginSuccess(String token);

        void onMainErrorCode(String msg);

        void onMainFail(Throwable e, boolean isNetWork);

        void getUserSuccess(BindUser data);

        void onCabinetInfoSuccess(RealmList<CabinetInfo> data);

        void temCabinetSuccess(CabinetBean cabinetBean);

    }

    private BaseService api;

    public MainController(MainControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }

    public void login(String userNmae, String password) {
        api.appLogin(userNmae, password)
                .compose(IOMainThread.<BaseEntity<String>>composeIO2main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccees(BaseEntity<String> t)  {
                        listener.onLoginSuccess(t.getData());
                    }

                    @Override
                    protected void onCodeError(String msg) {
                        listener.onMainErrorCode(msg);
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
            protected void onCodeError(String msg)  {
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
                .compose(IOMainThread.<BaseEntity<CabinetBean>>composeIO2main())
                .subscribe(new BaseObserver<CabinetBean>() {

                    @Override
                    protected void onSuccees(BaseEntity<CabinetBean> t) {
                        ToastMaster.shortToast(t.getSecondMessage());
                    }

                    @Override
                    protected void onCodeError(String msg) {
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

        api.temCabinet(retrunCabinetRequest).compose(IOMainThread.<BaseEntity<CabinetBean>>composeIO2main()).subscribe(new BaseObserver<CabinetBean>() {

            @Override
            protected void onSuccees(BaseEntity<CabinetBean> t) {
                listener.temCabinetSuccess(t.getData());
            }

            @Override
            protected void onCodeError(String msg)  {
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
                    protected void onCodeError(String msg) {

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
                               protected void onCodeError(String msg) {
                                   listener.onMainErrorCode(msg);
                               }

                               @Override
                               protected void onFailure(Throwable e, boolean isNetWorkError) {
                                   listener.onMainFail(e, isNetWorkError);
                               }
                           }
                );
    }
}
