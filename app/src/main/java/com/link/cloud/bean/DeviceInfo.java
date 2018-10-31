package com.link.cloud.bean;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/10/28.
 */

public class DeviceInfo extends RealmObject {
    String deviceId;
    String psw;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;
}
