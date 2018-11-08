package com.link.cloud.network.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/10/28.
 */

public class CabinetInfo extends RealmObject implements Serializable{


    /**
     * id : 386
     * lockId : 82
     * deviceId : 7
     * lineNo : 1
     * cabinetNo : 1
     * locked : true
     * uuid : 8148b486b35e4dd09315d6b5685f06fa
     * nickname :
     * phone :
     * startTime : 2018-11-06 00:00:00
     * endTime : 2018-11-16 00:00:00
     * vip : true
     * lockNo : 1
     * passwd : null
     */

    private int id;
    private int lockId;
    private int deviceId;
    private int lineNo;
    private String cabinetNo;
    private boolean locked;
    private String uuid;
    private String nickname;
    private String phone;
    private String startTime;
    private String endTime;
    private boolean vip;
    private int lockNo;
    private String passwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public int getLockNo() {
        return lockNo;
    }

    public void setLockNo(int lockNo) {
        this.lockNo = lockNo;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
