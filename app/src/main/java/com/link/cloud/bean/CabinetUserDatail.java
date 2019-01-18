package com.link.cloud.bean;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2019/1/18.
 */

public class CabinetUserDatail extends RealmObject{
    public String userName;
    public String phoneNum;
    String cabinetNo;

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getOpenOrClose() {
        return openOrClose;
    }

    public void setOpenOrClose(String openOrClose) {
        this.openOrClose = openOrClose;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String userTime;
    public String openOrClose;
    public long creatTime;
}
