package com.link.cloud;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.utils.Venueutils;
import com.zitech.framework.BaseApplication;
import com.zitech.framework.utils.ToastMaster;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;
    public static Venueutils venueUtils;

    public SerialPort serialPortOne = null;
    public SerialPort serialPortTwo = null;
    public SerialPort serialPortThree = null;
    public SpeechSynthesizer mTts;
    private User user;
    public static String voicerLocal="xiaoyan";

    public static Venueutils getVenueUtils() {
        synchronized (Venueutils.class) {
            if (venueUtils == null) {
                venueUtils = new Venueutils();
            }
            return venueUtils;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        user = new User();
        HttpConfig.TOKEN="";
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cabinet.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        intSerialPort();
        intSpeak();
    }


    public User getUser() {
        return user;
    }


    private void intSpeak(){
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int i) {
                ToastMaster.shortToast(String.valueOf(i));
                if (i != ErrorCode.SUCCESS) {
                    ToastMaster.shortToast(R.string.mTts_stating_error);
                }else {
                    setParam();
                    mTts.startSpeaking("哈哈", new SynthesizerListener() {
                        @Override
                        public void onSpeakBegin() {

                        }

                        @Override
                        public void onBufferProgress(int i, int i1, int i2, String s) {

                        }

                        @Override
                        public void onSpeakPaused() {

                        }

                        @Override
                        public void onSpeakResumed() {

                        }

                        @Override
                        public void onSpeakProgress(int i, int i1, int i2) {

                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {

                        }

                        @Override
                        public void onEvent(int i, int i1, int i2, Bundle bundle) {

                        }
                    });
                }
            }
        });
//
//
//        mTts.startSpeaking(getResources().getString(R.string.initialization_successful), new SynthesizerListener() {
//            @Override
//            public void onSpeakBegin() {
//
//            }
//
//            @Override
//            public void onBufferProgress(int i, int i1, int i2, String s) {
//
//            }
//
//            @Override
//            public void onSpeakPaused() {
//
//            }
//
//            @Override
//            public void onSpeakResumed() {
//
//            }
//
//            @Override
//            public void onSpeakProgress(int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onCompleted(SpeechError speechError) {
//
//            }
//
//            @Override
//            public void onEvent(int i, int i1, int i2, Bundle bundle) {
//
//            }
//        });
    }

    private void setParam(){

        // 清空参数

        mTts.setParameter(SpeechConstant.PARAMS, null);

        //设置合成

        //设置使用本地引擎

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);

        //设置发音人资源路径

        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());

        //设置发音人

        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);

        //设置合成语速

        mTts.setParameter(SpeechConstant.SPEED,"50");

        //设置合成音调

        mTts.setParameter(SpeechConstant.PITCH, "50");

        //设置合成音量

        mTts.setParameter(SpeechConstant.VOLUME, "50");

        //设置播放器音频流类型

        mTts.setParameter(SpeechConstant.STREAM_TYPE, "50");

        // 设置播放合成音频打断音乐播放，默认为true

        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限

        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效

        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");

    }


    //获取发音人资源路径

    private String getResourcePath(){

        StringBuffer tempBuffer = new StringBuffer();

        //合成通用资源

        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));

        tempBuffer.append(";");

        //发音人资源

        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+voicerLocal+".jet"));

        return tempBuffer.toString();

    }


    private void intSerialPort() {

        try {
            serialPortOne = new SerialPort(new File("/dev/ttysWK1"), 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortTwo = new SerialPort(new File("/dev/ttysWK2"), 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortThree = new SerialPort(new File("/dev/ttysWK3"), 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void post(Runnable r) {
        mainThreadHandler.post(r);
    }

    public static CabinetApplication getInstance() {
        return (CabinetApplication) BaseApplication.getInstance();
    }

}
