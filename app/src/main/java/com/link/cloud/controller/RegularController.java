package com.link.cloud.controller;

import android.support.v4.widget.EdgeEffectCompat;

import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.BaseService;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.link.cloud.network.bean.APPVersionBean;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.EdituserRequest;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.network.bean.RetrunCabinetRequest;

import okhttp3.RequestBody;

/**
 * 作者：qianlu on 2018/11/7 11:10
 * 邮箱：zar.l@qq.com
 */
public class RegularController {

    RegularController.RegularControllerListener regularControllerListener;
    private BaseService api;


    public interface RegularControllerListener {

        void successful(AllUser allUser);
        void qrSuccess(EdituserRequest allUser);

        void failed(String message,String code);

        void onRegularFail(Throwable e, boolean isNetWork);

        void openByFingerPrints();

        void onPassWord(PasswordBean passwordBean);
    }

    public void password(String pass){
        api.validatePassword(pass).compose(IOMainThread.<BaseEntity<PasswordBean>>composeIO2main()).subscribe(new BaseObserver<PasswordBean>() {


            @Override
            protected void onSuccees(BaseEntity<PasswordBean> t) {
                regularControllerListener.onPassWord(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                regularControllerListener.failed(msg,codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                regularControllerListener.onRegularFail(e, isNetWorkError);
            }
        });
    }
    public RegularController(RegularControllerListener regularControllerListener) {
        api = RetrofitFactory.getInstence().API();
        this.regularControllerListener = regularControllerListener;
    }


    public void findUser(String finger) {
        RetrunCabinetRequest request=new RetrunCabinetRequest();
        request.setFingerprint(finger);
        api.findUser(request).compose(IOMainThread.<BaseEntity<AllUser>>composeIO2main()).subscribe(new BaseObserver<AllUser>() {

            @Override
            protected void onSuccees(BaseEntity<AllUser> t) {
                regularControllerListener.successful(t.getData());
            }

            @Override
            protected void onCodeError(String msg, String codeErrorr) {
                regularControllerListener.failed(msg,codeErrorr);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) {
                regularControllerListener.onRegularFail(e, isNetWorkError);
            }
        }); }




}
