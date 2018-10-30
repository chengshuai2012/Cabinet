package com.link.cloud.network.bean;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/10/28.
 */

public class CabinetInfo extends RealmObject{

    /**
     * id : 101
     * lockId : 48
     * deviceId : 3
     * lineNo : 1
     * cabinetNo : 1
     * locked : false
     * uuid :
     * startTime : null
     * endTime : null
     * vip : false
     * lockNo : 1
     */

    private int id;
    private int lockId;
    private int deviceId;
    private int lineNo;
    private String cabinetNo;
    private boolean locked;
    private String uuid;
    private String startTime;
    private String endTime;
    private boolean vip;
    private int lockNo;

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
}