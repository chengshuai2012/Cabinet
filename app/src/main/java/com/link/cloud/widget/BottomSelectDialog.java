package com.link.cloud.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.link.cloud.R;
import com.zitech.framework.widget.ValidDialog;

/**
 * @author qianlu
 * @date 2018/9/26.
 * GitHub：qiandailu
 * email：zar.l@qq.com
 * description：
 */
public class BottomSelectDialog extends ValidDialog {
    private OnPositiveButtonClickListener onPositiveButtonClickListener;
    private OnCancelButtonClickListener onCancelButtonClickListener;

    public interface OnPositiveButtonClickListener {
        public void onClick(Dialog dialog);
    }

    public interface OnCancelButtonClickListener {
        public void onClick(Dialog dialog);
    }

    public BottomSelectDialog(Context context) {
        super(context, R.style.BottomPushDialog);
        setContentView(R.layout.dialog_bottomselect);
        WindowManager.LayoutParams param = getWindow().getAttributes();
        param.gravity = Gravity.BOTTOM;
        param.height = WindowManager.LayoutParams.WRAP_CONTENT;
        param.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes(param);
        initialize();
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onNegativeButtonClickListener) {
        this.onCancelButtonClickListener = onNegativeButtonClickListener;
    }

    private void initialize() {


    }


}

