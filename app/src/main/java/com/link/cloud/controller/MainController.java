package com.link.cloud.controller;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.APPVersionBean;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;
import okhttp3.RequestBody;

/**
 * Created by 49488 on 2018/12/3.
 */

public class MainController {
    MainControllerListener listener;

    public interface MainControllerListener {

        void onMainErrorCode(String msg);

        void onMainFail(Throwable e, boolean isNetWork);

        void MainPassWord(PasswordBean passwordBean);
    }

    private BaseService api;

    public MainController(MainControllerListener listener) {
        api = RetrofitFactory.getInstence().API();
        this.listener = listener;
    }
    public void password(String pass){
        api.validatePassword(pass).compose(IOMainThread.<BaseEntity<PasswordBean>>composeIO2main()).subscribe(new BaseObserver<PasswordBean>() {


            @Override
            protected void onSuccees(BaseEntity<PasswordBean> t) {
                listener.MainPassWord(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                listener.onMainErrorCode(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                listener.onMainFail(e, isNetWorkError);
            }
        });
    }
}
