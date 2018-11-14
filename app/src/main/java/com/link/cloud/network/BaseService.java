package com.link.cloud.network;

import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.link.cloud.network.bean.RequestBindFinger;
import com.link.cloud.network.bean.RetrunCabinetRequest;
import com.link.cloud.network.bean.SingleUser;

import io.reactivex.Observable;
import io.realm.RealmList;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by OFX002 on 2018/10/28.
 */

public interface BaseService {
    /**
     * 登陆接口
     */

    @POST(ApiConstants.APPLOGIN)
    Observable<BaseEntity<CabnetDeviceInfoBean>> appLogin(@Path("deviceCode") String deviceCode, @Path("password") String password);

    /**
     * 获取验证码
     */
    @GET(ApiConstants.SENDVCODE)
    Observable<BaseEntity> sendVCode(@Path("phone") String phone);

    /**
     * 退柜
     */
    @POST(ApiConstants.RETURNCABINET)
    Observable<BaseEntity<CabinetInfo>> returnCabinet(@Body RetrunCabinetRequest retrunCabinetRequest);

    /**
     * 续柜
     */
    @POST(ApiConstants.USECABINET)
    Observable<BaseEntity> useCabinet(@Body RetrunCabinetRequest retrunCabinetRequest);

    /**
     * 临时柜
     */
    @POST(ApiConstants.TEMCABINET)
    Observable<BaseEntity<CabinetInfo>> temCabinet(@Body RetrunCabinetRequest retrunCabinetRequest);

    /**
     * 获取用户
     */
    @POST(ApiConstants.GETUSERS)
    Observable<BaseEntity<BindUser>> getUser(@Body RequestBindFinger requestBindFinger);

    /**
     * 获取柜子配置信息
     */
    @GET(ApiConstants.GETCABINETINFO)
    Observable<BaseEntity<RealmList<CabinetInfo>>> getCabinetInfo();

    /**
     * Vip开柜
     */
    @POST(ApiConstants.VIPCABINET)
    Observable<BaseEntity<CabinetInfo>> VIPCabinet(@Body RetrunCabinetRequest retrunCabinetRequest);

    /**
     * 获取单个用户信息
     */
    @POST(ApiConstants.VALIDATEFINGERPRINTS)
    Observable<BaseEntity<AllUser>> findUser(@Body RetrunCabinetRequest retrunCabinetRequest);


    /**
     * 获取单个用户指静脉
     */
    @GET(ApiConstants.GETSINGLEUSER)
    Observable<BaseEntity<SingleUser>> findOneUserFinger(@Path("uuid") String uuid);


    /**
     * 验证机器密码
     *
     * @param
     * @returnscheduleReport
     * @see
     */
    @POST(ApiConstants.VALIDATEPASS)
    Observable<BaseEntity> validatePassword(@Path("password") String password);


}
