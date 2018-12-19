package com.link.cloud.network.bean;

/**
 * Created by 49488 on 2018/12/19.
 */

public class PassWordValidate {

    /**
     * id : 322
     * merchantId : 7
     * uid : 46
     * uuid : 2168d56b9e544ed5942c673470ba76c0
     * phone : 17665222565
     * sex : 0
     * headImg : http://jsxt.zlla.cn/employeesImg/default.png
     * userType : 3
     * status : 1
     * createTime : 2018-12-17 16:17:05
     * nickname : 测试账户1
     * isadmin : 1
     * admission : null
     * bookid : null
     */

    private int id;
    private int merchantId;
    private String uid;
    private String uuid;
    private String phone;
    private int sex;
    private String headImg;
    private int userType;
    private int status;
    private String createTime;
    private String nickname;
    private int isadmin;
    private Object admission;
    private Object bookid;

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

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public Object getAdmission() {
        return admission;
    }

    public void setAdmission(Object admission) {
        this.admission = admission;
    }

    public Object getBookid() {
        return bookid;
    }

    public void setBookid(Object bookid) {
        this.bookid = bookid;
    }
}
