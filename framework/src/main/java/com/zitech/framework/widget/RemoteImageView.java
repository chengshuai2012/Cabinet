package com.zitech.framework.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

/**
 * @author LuDaiqian
 */
public class RemoteImageView extends ImageView {

    private static final int NONE = -1;
    /**
     * 网络图片url
     */
    private String mUrl;

    private int mDefaultImageResource = NONE;
    private Drawable mDefaultDrawable;
    private Transformation<Bitmap> bitmapTransformation;
    private int mErrorImageResource = NONE;
    private Drawable mErrorDrawable;
    private OnErrorListener mOnErrorListener;

    public String getImageUri() {
        return mUrl;
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RemoteImageView(Context context) {
        super(context);
    }


    /**
     * 加载网络图片
     *
     * @param url eg. http://yourwebsite.com/abz.jpg
     */
    public void setImageUri(final String url) {
        if (isActivityDestoryed())
            return;
        if (TextUtils.isEmpty(url)) {
            if (mDefaultDrawable != null) {
                setImageDrawable(mDefaultDrawable);
            } else if (mDefaultImageResource != NONE) {
                setImageResource(mDefaultImageResource);
            }
            return;
        }
        mUrl = url;
        DrawableTypeRequest<String> request = Glide.with(getContext()).load(url);
        if (mDefaultDrawable != null) {
            request.placeholder(mDefaultDrawable);
        } else if (mDefaultImageResource != NONE) {
            request.placeholder(mDefaultImageResource);
        }
        if (bitmapTransformation != null) {
            request.bitmapTransform(bitmapTransformation);
        }
        if (mErrorDrawable != null) {
            request.error(mErrorDrawable);
        } else if (mErrorImageResource != NONE) {
            request.error(mErrorImageResource);
        }
        request.listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if (mOnErrorListener != null) {
                    mOnErrorListener.onLoadError(url);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        });
        request.into(this);
    }

    public interface OnErrorListener {
        public void onLoadError(String url);
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    @SuppressLint("NewApi")
    private boolean isActivityDestoryed() {
        Context context = getContext();
        if (context instanceof Activity) {
            return ((Activity) context).isFinishing();
        }
        return false;
    }

    public void setDefaultImageDrawable(Drawable defaultDrawable) {
        this.mDefaultDrawable = defaultDrawable;
    }

    @SuppressWarnings("deprecation")
    public void setDefaultImageBitmap(Bitmap defaultImageBitmap) {
        this.mDefaultDrawable = new BitmapDrawable(defaultImageBitmap);
    }

    /**
     * 设置默认图片资源id
     *
     * @param resId
     */
    public void setDefaultImageResource(int resId) {
        mDefaultImageResource = resId;
    }

    public void setImageUri(int defResId, String url) {
//        if (mUrl == null || !this.mUrl.equals(url)) {
        setDefaultImageResource(defResId);
        setImageUri(url);
//        }
    }

    public void setImageUri(Drawable defaultDrawable, String url) {
        if (mUrl == null || !this.mUrl.equals(url)) {
            setDefaultImageDrawable(defaultDrawable);
            setImageUri(url);
        }
    }

    public int getErrorImageResource() {
        return mErrorImageResource;
    }


    public void setErrorImageResource(int errorImageResource) {
        this.mErrorImageResource = errorImageResource;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(Drawable errorDrawable) {
        this.mErrorDrawable = errorDrawable;
    }

    public void setBitmapTransformation(Transformation<Bitmap> tf) {
        this.bitmapTransformation = tf;
    }
}
