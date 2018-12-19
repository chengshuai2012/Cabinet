package com.link.cloud.network.bean;

/**
 * 作者：qianlu on 2018/10/15 15:35
 * 邮箱：zar.l@qq.com
 */
public class EdituserRequest {


    /**
     * createTime : 2018-10-15T07:04:23.818Z
     * fingerprint : string
     * headImg : string
     * id : 0
     * merchantId : 0
     * nickname : string
     * phone : string
     * sex : 0
     * status : 0
     * uid : string
     * userType : 0
     * uuid : string
     */

    private String createTime;
    private String fingerprint;
    private String headImg;
    private int id;
    private int merchantId;
    private String nickname;
    private String phone;
    private int sex;
    private int status;
    private String uid;
    private int userType;
    private String uuid;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
