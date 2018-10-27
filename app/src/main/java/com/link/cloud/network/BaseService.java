package com.link.cloud.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by OFX002 on 2018/10/28.
 */

public interface BaseService {
    /**登陆接口*/

    @POST(ApiConstants.APPLOGIN)
    Observable<BaseEntity<String>> appLogin(@Path("deviceCode") String deviceCode, @Path("password") String password);

    /**获取验证码*/
    @GET(ApiConstants.SENDVCODE)
    Observable<BaseEntity> sendVCode( @Path("phone") String phone);
}
