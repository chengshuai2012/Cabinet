package com.link.cloud.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.link.cloud.CabinetApplication;
import com.link.cloud.activity.SplashActivity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.RealmObject;

/**
 * Created by OFX002 on 2018/10/28.
 */

public abstract class ArrayObserver<T extends RealmObject> implements Observer<ArrayEntity<T >> {
    protected Context mContext;


    public ArrayObserver() {
        this.mContext= CabinetApplication.getInstance();
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();

    }

    @Override
    public void onNext(ArrayEntity<T> tArrayEntity) {
                if (tArrayEntity.isSuccess()) {
            onSuccees(tArrayEntity);
        } else {
            if("400000999102".equals(tArrayEntity.getCode())){
                Toast.makeText(mContext,tArrayEntity.getMsg(),Toast.LENGTH_LONG).show();
//                Intent intent2 = new Intent(mContext, SplashActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent2);
//                android.os.Process.killProcess(android.os.Process.myPid());
            }else {
                onCodeError(tArrayEntity.getSecondMessage()+tArrayEntity.getMsg(),tArrayEntity.getCode());
            }

        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ConnectException || e instanceof TimeoutException || e instanceof NetworkErrorException || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
            Log.e("onError: ",e.getMessage() );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(ArrayEntity<T>t ) ;

    /**
     * 返回成功了,但是code错误
     *
     * @param
     * @throws Exception
     */
    protected abstract void onCodeError(String msg,String codeErrorr) ;

    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError);

    protected void onRequestStart() {

    }





}
