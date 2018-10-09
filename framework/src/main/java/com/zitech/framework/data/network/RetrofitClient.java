package com.zitech.framework.data.network;


import com.zitech.framework.BaseApplication;
import com.zitech.framework.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {


    //短缓存有效期为1分钟
    public static final int CACHE_STALE_SHORT = 60;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
    public static final String CONTENT_TYPE = "Content-Type:";
    public static final String JSON = "application/json;charset=utf-8";
    public static final String FORM = "application/x-www-form-urlencoded";


    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";
    //    private static final String ROOT_URL = "";
    private static OkHttpClient mOkHttpClient;
    private static boolean debug = true;

    private HashMap<String, Retrofit> mRetrofits;

    private static volatile RetrofitClient instance = null;

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }


    private RetrofitClient() {
        mRetrofits = new HashMap<>();
        initOkHttpClient();
//        mRetrofits.put()
    }

    public static void setDebug(boolean debug) {
        RetrofitClient.debug = debug;
    }


    public Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                //拓展添加RxJava的功能，导入的库：compile 'com.squareup.retrofit2:adapter-rxjava:2.0.2'
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //使用Gson对Json进行解析，导入的库：compile 'com.squareup.retrofit2:converter-gson:2.0.2'
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(String baseUrl, Class<T> mClass) {
        if (!mRetrofits.containsKey(baseUrl)) {
            mRetrofits.put(baseUrl, createRetrofit(baseUrl));
        }
        return mRetrofits.get(baseUrl).create(mClass);
    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClient == null) {

                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(BaseApplication.getInstance().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache);
                    //拓展功能：网络请求的log，compile 'com.squareup.okhttp3:logging-interceptor:3.2.0'
//                            .addInterceptor(mRewriteCacheControlInterceptor)
//                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    if (debug) {
                        builder.addInterceptor(interceptor);
                    }

                    SSLContext ctx = null;
                    try {
                        ctx = SSLContext.getInstance("SSL");
                        ctx.init(new KeyManager[0], new TrustManager[]{new TrustAllCertsManager()}, new SecureRandom());
                    } catch (KeyManagementException | NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
                    builder.sslSocketFactory(sslSocketFactory);
                    builder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

//                            .cookieJar(new CookiesManager())
                    mOkHttpClient = builder.retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();


                }
            }
        }
    }

    private static class TrustAllCertsManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkAvailable()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            okhttp3.Response originalResponse = chain.proceed(request);
            originalResponse.header("Set-Cookie");

            if (NetworkUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma").build();
            }
        }
    };

    /**
     * 自动管理Cookies
     */
    private class CookiesManager implements CookieJar {
        private final PersistentCookieCache cookieStore = new PersistentCookieCache(BaseApplication.getInstance(), true);

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
