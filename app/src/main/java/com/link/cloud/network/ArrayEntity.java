package com.link.cloud.network;

import com.google.gson.annotations.JsonAdapter;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/11/7.
 */

public class ArrayEntity<T extends RealmObject>  {
    private static String SUCCESS_CODE="200000";
    private String code;
    private String message;
    private String secondMessage;
    @JsonAdapter(ArrayListAdapter.class)
    private RealmList<T> data;

    public void setData(RealmList<T> data) {
        this.data = data;
    }


    public String getSecondMessage() {
        return secondMessage;
    }

    public void setSecondMessage(String secondMessage) {
        this.secondMessage = secondMessage;
    }

    public boolean isSuccess(){
        return getCode().equals(SUCCESS_CODE);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public RealmList<T> getData() {
        return data;
    }
}
