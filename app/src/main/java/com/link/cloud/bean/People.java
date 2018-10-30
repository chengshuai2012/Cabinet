package com.link.cloud.bean;


import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/10/15.
 */

public class People extends RealmObject {

    /**
     * id : 17
     * merchantId : 1
     * uid : i2wk31hg
     * uuid : 406afce8a19b453bb77f6312a14b5a8b
     * phone : 13726261348
     * sex : 1
     * headImg :
     * fingerprint : null
     * userType : 1
     * status : 1
     * createTime : 2018-10-20 18:21:03
     * nickname :
     */

    private int id;
    private int merchantId;
    private String uid;
    private String uuid;
    private String phone;
    private int sex;
    private String headImg;
    private String fingerprint;
    private int userType;
    private int status;
    private String createTime;
    private String nickname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
