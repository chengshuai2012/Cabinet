package com.link.cloud.network;


/**
 * Created by qianlu on 2018/5/16.
 * 邮箱：zar.l@qq.com
 */
public class ApiConstants {

    //APP登录
    public static final String APPLOGIN = "/{deviceCode}/{password}";

    //获取验证码
    public static final String SENDVCODE = "sendVCode/{phone}";

    //退柜
    public static final String RETURNCABINET = "returnCabinet";

    //临时柜号
    public static final String TEMCABINET = "temporaryCabinet";

    //续开柜号
    public static final String USECABINET = "useCabinet";

    //分页获取指静脉
    public static final String GETUSERS = "users";

    //获取柜子配置信息
    public static final String GETCABINETINFO = "cabinetInfo";

    //VIP开柜
    public static final String VIPCABINET = "useCabinetVip";

    //验证指纹信息
    public static final String VALIDATEFINGERPRINTS = "/app/validateFingerprints";

    //获取单独用户
    public static final String GETSINGLEUSER = "user/{uuid}";


    //验证机器密码
    public static final String VALIDATEPASS = "/app/validatePassword/{password}";
//
//    //二维码开柜
    public static final String QROPEN = "bindUserByQrCode";
   //二维码开柜
  //  public static final String QROPEN = "useCabinetByQrCode/{type}";

    //获取APP版本
    public static final String APPVERSION = "appVersion/{appType}";

    //下载App
    public static final String DOWNLOAD = "downloadApp/{appType}";

    //验证密码
    public static final String PASSWORD = "checkPwd/{uuid}/{pwd}";

}
