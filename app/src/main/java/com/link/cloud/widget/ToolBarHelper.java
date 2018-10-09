package com.link.cloud.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.zitech.framework.utils.ViewUtils;


/**
 * @author lidong on 2016/2/15.
 * @ClassName ToolBarHelper
 * @描述：用于处理ToolBar
 */
public class ToolBarHelper {

    /*上下文，创建view的时候需要用到*/
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    /*toolbar*/
    private Toolbar mToolBar;


    /*视图构造器*/
    private LayoutInflater mInflater;

    /*
     * 两个属性
     * 1、toolbar是否悬浮在窗口之上
     * 2、toolbar的高度获取
     * */
    private static int[] ATTRS = {

            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    private ImageView mTitle;

    private TextView mMenuText;
    private TextView mNavigationText;
    private boolean menuIconVisible = true;
    private int menuIcon;

    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init();
        initAppBar();
        initContentLayout(layoutId);


    }

    /*初始化整个内容*/
    private void init() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mContentView.setFitsSystemWindows(true);
    }

    /*初始化用户定义的布局*/
    private void initAppBar() {
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.toolbar, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.tool_bar);
        mTitle = (ImageView) toolbar.findViewById(R.id.toolbar_title);
        mToolBar.setTitle("");
        mMenuText = (TextView) toolbar.findViewById(R.id.menuText);
        mNavigationText = (TextView) toolbar.findViewById(R.id.navigationText);
    }

    /*初始化toolbar*/
    private void initContentLayout(int id) {
        mUserView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(android.support.v7.appcompat.R.styleable.Theme);
        /*获取主题中定义的悬浮标志*/
        // boolean overly = typedArray.getBoolean(android.support.v7.appcompat.R.styleable.Theme_windowActionBarOverlay, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = mContext.getResources().getDimensionPixelOffset(R.dimen.action_bar_size);
        //(int) typedArray.getDimension(android.support.v7.appcompat.R.styleable.Theme_actionBarSize, (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        // typedArray.recycle();
        params.topMargin = toolBarSize;
        if (mUserView.getBackground() == null) {
            mUserView.setBackgroundResource(R.color.windowBackgroundLight);
        }
        mContentView.addView(mUserView, params);


    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUserView.getLayoutParams();
        params.topMargin = 0;

    }

    public void setmToolBarHeight(int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUserView.getLayoutParams();
        params.height = height;

        ViewGroup.LayoutParams mToolBarParams = mToolBar.getLayoutParams();
        mToolBarParams.height=height;

        mToolBar.setLayoutParams(mToolBarParams);
        mUserView.setLayoutParams(params);
    }

    public void setTitle(int titleResId) {
        mTitle.setImageResource(titleResId);
    }

    public void setNavigationOnClickListener(View.OnClickListener navigationOnClickListener) {
        mToolBar.setNavigationOnClickListener(navigationOnClickListener);
    }

    public void hideNavigationIcon() {
        mToolBar.setNavigationIcon(null);
    }

    public void setNavigationIcon(int navigationIcon) {
        mToolBar.setNavigationIcon(navigationIcon);
    }

    public void setNavigationIcon(Drawable navigationIcon) {
        mToolBar.setNavigationIcon(navigationIcon);
    }

    public void setMenuTextVisibility(int menuTextVisibility) {
        this.mMenuText.setVisibility(menuTextVisibility);
    }

    public boolean isMenuIconVisible() {
        return menuIcon != 0 ? menuIconVisible : false;
    }

    public void setMenuIconVisible(boolean menuIconVisible) {
        this.menuIconVisible = menuIconVisible;
    }

    public void setMenuText(int textResId) {
        mMenuText.setText(textResId);
    }

    public void setMenuText(String text) {
        mMenuText.setText(text);
    }

    public void setOnMenuTextClickListener(View.OnClickListener onMenuTextClickListener) {
        ViewUtils.setOnClickListener(mMenuText, onMenuTextClickListener);
    }

    public void setNavigationText(String navigationText) {
        mNavigationText.setText(navigationText);
    }

    public void setNavigationText(int textResId) {
        mNavigationText.setText(textResId);
    }

    public void setNavigationTextClickListener(View.OnClickListener navigationTextClickListener) {
        mNavigationText.setOnClickListener(navigationTextClickListener);
    }

    public void setMenuIcon(int menuIconResId) {
        this.menuIcon = menuIconResId;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public boolean hasMenuIcon() {
        return menuIcon != 0;
    }

    public void setNavigationTextVisibility(int navigationTextVisible) {
        mNavigationText.setVisibility(navigationTextVisible);
    }
}

