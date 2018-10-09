package com.zitech.framework.data.network.subscribe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.zitech.framework.utils.ToastMaster;
import com.zitech.framework.utils.Utils;
import com.zitech.framework.widget.LoadingDialog;

import rx.Subscriber;

//import com.zitech.framework.data.network.IContext;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public abstract class ProgressSubscriber<T> extends Subscriber<T> {

    private Dialog mDialog;

    private Context context;

    public ProgressSubscriber(Context context) {
        this(context, new LoadingDialog(context));
    }

    public ProgressSubscriber(Context context, Dialog dialog) {
        this.context = context;
        mDialog = dialog;
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (!isUnsubscribed()) {
                    unsubscribe();
                }
            }
        });
    }

    private void showProgressDialog() {
        mDialog.show();
    }

    protected void dismissProgressDialog() {
        mDialog.dismiss();
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        // Toast.makeText(context.getContext(), "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        ToastMaster.shortToast(Utils.parseError(e));
        dismissProgressDialog();

    }


}