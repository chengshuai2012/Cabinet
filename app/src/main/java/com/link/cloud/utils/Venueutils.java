package com.link.cloud.utils;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.link.cloud.bean.FingerprintsBean;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.veune.MdDevice;
import com.link.cloud.veune.MdUsbService;
import com.link.cloud.veune.ModelImgMng;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import md.com.sdk.MicroFingerVein;


/**
 * Created by 49488 on 2018/10/15.
 */

public class Venueutils {
    public MdUsbService.MyBinder mdDeviceBinder;
    public byte[] img;
    Context context;
    VenueCallBack callBack;
    private boolean bOpen = false;//设备是否打开
    private int[] pos = new int[1];
    private float[] score = new float[1];
    private boolean ret;
    public ModelImgMng modelImgMng = new ModelImgMng();
    private int[] tipTimes = {0, 0};//后两次次建模时用了不同手指或提取特征识别时，最多重复提醒限制3次
    private int lastTouchState = 0;//记录上一次的触摸状态
    private int modOkProgress = 0;
    private final static float IDENTIFY_SCORE_THRESHOLD = 0.63f;
    private final static float MODEL_SCORE_THRESHOLD = 0.4f;
    public interface VenueCallBack{
        void modelMsg(int state, String msg);
    }
    public  void initVenue(Context context, VenueCallBack callBack,  Boolean bOpen){
        this.bOpen=bOpen;
        this.context=context;
        if(mdDeviceBinder==null){
            Intent intent = new Intent(context, MdUsbService.class);
            context.bindService(intent, mdSrvConn, Service.BIND_AUTO_CREATE);
        }
        this.callBack=callBack;
    }
    public int getState() {
        if (!bOpen) {
            modOkProgress = 0;
            modelImgMng.reset();
            Logger.e(mdDeviceBinder+"");
            bOpen = mdDeviceBinder.openDevice(0);//开启指定索引的设备
            if (bOpen) {
                Logger.e( "open device success");
            } else {
                Logger.e("open device failed,stop identifying and modeling.");

            }
        }
        int state = mdDeviceBinder.getDeviceTouchState(0);
        if (state != 3) {
            if (lastTouchState != 0) {
                mdDeviceBinder.setDeviceLed(0, MdUsbService.getFvColorRED(), true);
            }
            lastTouchState = 0;
        }
        if (state == 3) {
            //返回值state=3表检测到了双Touch触摸,返回1表示仅指腹触碰，返回2表示仅指尖触碰，返回0表示未检测到触碰
            if (lastTouchState == 3) {
                return 4;
            }
            lastTouchState = 3;
            mdDeviceBinder.setDeviceLed(0, MdUsbService.getFvColorGREEN(), false);
            img = mdDeviceBinder.tryGrabImg(0);
            Logger.e(HexUtil.bytesToHexString(img));
            if (img == null) {
                Logger.e("get img failed,please try again");
                return 0;
            }
        }
        return state;
    }

    List<AllUser> subListPeople = new ArrayList<>();




    public String identifyNewImg(final List<AllUser> peoples) {
        final int nThreads=peoples.size()/1000+1;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<String>> futures = new ArrayList();
        for (int i = 0; i < nThreads; i++) {
                if(i==nThreads-1){
                    subListPeople= peoples.subList(1000 * i, peoples.size());
                }else {
                    subListPeople= peoples.subList(1000 * i, 1000 * (i + 1));
                }
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuffer sb = new StringBuffer();
                    String[] uids = new String[1000];
                    int position =0;
                    for (AllUser userBean : subListPeople) {
                        sb.append(userBean.getFingerprint());
                        uids[position] = userBean.getUuid();
                        position++;

                    }
                    byte[] allFeaturesBytes = HexUtil.hexStringToByte(sb.toString());
                    boolean identifyResult = MicroFingerVein.fv_index(allFeaturesBytes, allFeaturesBytes.length / 3352, img, pos, score);
                    identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
                    if (identifyResult) {//比对通过且得分达标时打印此手指绑定的用户名
                        String uid = uids[pos[0]];
                        return uid;
                    } else {
                        return null;

                    }
                }
            };

            futures.add(executorService.submit(task));

        }
        for (Future<String> future : futures) {
            try {
                if(!TextUtils.isEmpty(future.get())){
                    return future.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return null;
    }
    List<FingerprintsBean> subListUser = new ArrayList<>();
    public String identifyNewImgUser(final ArrayList<FingerprintsBean> peoples) {
        final int nThreads=peoples.size()/1000+1;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future<String>> futures = new ArrayList();
        for (int i = 0; i < nThreads; i++) {
            if(i==nThreads-1){
                subListUser= peoples.subList(1000 * i, peoples.size());
            }else {
                subListUser= peoples.subList(1000 * i, 1000 * (i + 1));
            }

            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuffer sb = new StringBuffer();
                    String[] uids = new String[1000];
                    int position =0;
                    for (FingerprintsBean userBean : subListUser) {
                        sb.append(userBean.getFingerprint());
                        uids[position] = userBean.getUuid();
                        position++;

                    }
                    byte[] allFeaturesBytes = HexUtil.hexStringToByte(sb.toString());
                    boolean identifyResult = MicroFingerVein.fv_index(allFeaturesBytes, allFeaturesBytes.length / 3352, img, pos, score);
                    identifyResult = identifyResult && score[0] > IDENTIFY_SCORE_THRESHOLD;//得分是否达标
                    if (identifyResult) {//比对通过且得分达标时打印此手指绑定的用户名
                        String uid = uids[pos[0]];
                        return uid;
                    } else {
                        return null;

                    }
                }
            };

            futures.add(executorService.submit(task));

        }
        for (Future<String> future : futures) {
            try {
                if(!TextUtils.isEmpty(future.get())){
                    return future.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return null;
    }
    private List<MdDevice> mdDevicesList = new ArrayList<MdDevice>();
    public static MdDevice mdDevice;
    private final int MSG_REFRESH_LIST = 0;
    private Handler listManageH = new Handler(new Handler.Callback() {

        @Override

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_LIST: {
                    mdDevicesList.clear();
                    mdDevicesList = getDevList();
                    if (mdDevicesList.size() > 0) {
                        mdDevice = mdDevicesList.get(0);
                    } else {
                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST, 1500L);

                    }
                    break;
                }

            }
            return false;

        }

    });
    private List<MdDevice> getDevList() {
        List<MdDevice> mdDevList = new ArrayList<MdDevice>();
        if (mdDeviceBinder != null) {
            int deviceCount = MicroFingerVein.fvdev_get_count();
            for (int i = 0; i < deviceCount; i++) {
                MdDevice mdDevice = new MdDevice();
                mdDevice.setNo(i);
                mdDevice.setIndex(mdDeviceBinder.getDeviceNo(i));
                mdDevList.add(mdDevice);
            }
        } else {
            Logger.e( "microFingerVein not initialized by MdUsbService yet,wait a moment...");
        }
        return mdDevList;

    }
    private ServiceConnection mdSrvConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mdDeviceBinder = (MdUsbService.MyBinder) service;
            if (mdDeviceBinder != null) {
                mdDeviceBinder.setOnUsbMsgCallback(mdUsbMsgCallback);
                listManageH.sendEmptyMessage(MSG_REFRESH_LIST);
                Logger.e("bind MdUsbService success.");
            } else {
                Logger.e( "bind MdUsbService failed.");
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.e("disconnect MdUsbService.");
        }
    };

    private MdUsbService.UsbMsgCallback mdUsbMsgCallback = new MdUsbService.UsbMsgCallback() {
        @Override
        public void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName) {
            String newUsbInfo = "USB厂商：" + usbManufacturerName + "  \nUSB节点：" + usbDeviceName;
            Logger.e(newUsbInfo);
        }
        @Override
        public void onUsbDisconnect() {
            Logger.e("USB连接已断开");
        }

    };
    public void unBindService(){
        context.unbindService(mdSrvConn);
    }

}
