package com.link.cloud.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.link.cloud.R;
import com.zitech.framework.utils.ViewUtils;
import com.zitech.framework.widget.ValidDialog;

public class SimpleStyleDialog extends ValidDialog {
    private TextView contentView;
    private Button confirm;
    private Button cancel;
    private OnPositiveButtonClickListener onPositiveButtonClickListener;
    private OnCancelButtonClickListener onCancelButtonClickListener;
    private View divider;
    private CheckBox agree;

    public SimpleStyleDialog(Context context, int resId) {
        this(context, context.getString(resId));
    }


    private TextView titleView;

    public interface OnPositiveButtonClickListener {
        public void onClick(Dialog dialog);
    }

    public interface OnCancelButtonClickListener {
        public void onClick(Dialog dialog);
    }

    public SimpleStyleDialog(Context context, CharSequence content) {
        super(context, R.style.CommonDialog);
        setContentView(R.layout.dialog_simple_style);
        contentView = (TextView) findViewById(R.id.content);
        titleView = (TextView) findViewById(R.id.title);
        divider = findViewById(R.id.divider);
        // titleView.setText(title);
        agree = (CheckBox) findViewById(R.id.agree);
        contentView.setText(content);
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancle);

        ViewUtils.setOnClickListener(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onPositiveButtonClickListener != null)
                    onPositiveButtonClickListener.onClick(SimpleStyleDialog.this);
            }
        });
        ViewUtils.setOnClickListener(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelButtonClickListener != null) {
                    onCancelButtonClickListener.onClick(SimpleStyleDialog.this);
                }
                cancel();
            }
        });
    }

    public void setTitle(String title) {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(title);
    }

    public void setContent(String content) {
        contentView.setText(content);
    }

    public void setContent(String content, int color) {
        contentView.setText(content);
        contentView.setTextColor(color);
    }

    public void setConfirmStyle(boolean confirmStyle) {
        if (confirmStyle) {
            cancel.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
    }

    public void setOnAgreeCheckedChangeListener(CompoundButton.OnCheckedChangeListener l) {
        agree.setVisibility(View.VISIBLE);
        agree.setOnCheckedChangeListener(l);
    }

    public void setPositiveButtonText(String text) {
        confirm.setText(text);
    }

    public void setPositiveButtonTextColor(int textColor) {
        confirm.setTextColor(textColor);
    }

    public void setPositiveButtonText(int resId) {
        confirm.setText(resId);
    }

    public void setCancelButtonText(String text) {
        cancel.setText(text);
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onNegativeButtonClickListener) {
        this.onCancelButtonClickListener = onNegativeButtonClickListener;
    }
}
