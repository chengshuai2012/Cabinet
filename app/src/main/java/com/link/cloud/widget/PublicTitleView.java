package com.link.cloud.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.zitech.framework.utils.ViewUtils;


public class PublicTitleView extends LinearLayout {

    private String titleTextString;
    private float titleTextSize;
    private int titleTextColor;
    private Integer titleTextBackground;

    private String hintTextString;
    private float hintextSize;
    private int hintTextColor;


    private String finshTextString;
    private float finshtextSize;
    private int finshTextColor;
    private Integer finishTextBackground;


    private TextView title;
    private TextView hintText;
    private TextView finsh;
    private Context context;


    private onItemClickListener itemClickListener;

    public interface onItemClickListener {
        void itemClickListener();
    }


    public PublicTitleView(Context context) {
        super(context);
        this.context = context;
    }

    public PublicTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Resources res = context.getResources();
        LayoutInflater.from(context).inflate(R.layout.title_common_layout, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PublicTitleView);

        titleTextString = a.getString(R.styleable.PublicTitleView_title_text);
        titleTextSize = a.getDimension(R.styleable.PublicTitleView_title_text_size, ViewUtils.getDimen(R.dimen.w20));
        titleTextColor = a.getColor(R.styleable.PublicTitleView_title_text_color, res.getColor(R.color.red));

        if (a.hasValue(R.styleable.PublicTitleView_finish_text_Background)) {
            titleTextBackground = a.getResourceId(R.styleable.PublicTitleView_finish_text_Background, -1);
        }
        hintTextString = a.getString(R.styleable.PublicTitleView_hint_text);
        hintextSize = a.getDimension(R.styleable.PublicTitleView_hint_text_size, ViewUtils.getDimen(R.dimen.w20));
        hintTextColor = a.getColor(R.styleable.PublicTitleView_hint_text_color, res.getColor(R.color.white));

        finshTextString = a.getString(R.styleable.PublicTitleView_finish_text);
        finshtextSize = a.getDimension(R.styleable.PublicTitleView_finish_text_size, ViewUtils.getDimen(R.dimen.w20));
        finshTextColor = a.getColor(R.styleable.PublicTitleView_finish_text_color, res.getColor(R.color.almost_white));
        if (a.hasValue(R.styleable.PublicTitleView_finish_text_Background)) {
            finishTextBackground = a.getResourceId(R.styleable.PublicTitleView_finish_text_Background, -1);
        }
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        hintText = (TextView) findViewById(R.id.hintText);


        finsh = (TextView) findViewById(R.id.finsh);
        finsh.setTextColor(finshTextColor);

//
        if (hasValue(titleTextBackground)) {
            finsh.setBackgroundResource(titleTextBackground);
        }


        title.setTextColor(titleTextColor);


        if (!TextUtils.isEmpty(titleTextString)) {
            title.setText(titleTextString);
            title.setVisibility(View.VISIBLE);
            title.setTextColor(titleTextColor);
        } else {
            title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(hintTextString)) {
            hintText.setText(hintTextString);
            hintText.setVisibility(View.VISIBLE);
            hintText.setTextColor(hintTextColor);
        } else {
            hintText.setVisibility(View.GONE);
        }
        ViewUtils.setOnClickListener(finsh, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != itemClickListener) {
                    itemClickListener.itemClickListener();
                }
            }
        });
    }

    private boolean hasValue(Object value) {
        return value != null;
    }

    public void hideBack(){
        finsh.setVisibility(INVISIBLE);
    }

    /**
     * 设置此控件的文本
     *
     * @param text
     */
    public void setTitleText(String text) {
        title.setText(text);
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    public void setTitleTextColor(int textColor) {
        title.setTextColor(textColor);
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTitleTextSize(int textSize) {
        title.setTextSize(textSize);
    }

    /**
     * 返钮监听事件
     *
     * @param itemClickListener
     */
    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 设置此控件的文本
     *
     * @param text
     */
    public void setFinshText(String text) {
        finsh.setText(text);
    }


    /**
     * 设置标题可见
     *
     * @param
     */
    public void setFinsh(int visi) {
        finsh.setVisibility(visi);
    }


    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    public void setFinshTextColor(int textColor) {
        finsh.setTextColor(textColor);
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setFinshTextSize(int textSize) {
        finsh.setTextSize(textSize);
    }


}
