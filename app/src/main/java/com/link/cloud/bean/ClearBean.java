package com.link.cloud.bean;

/**
 * Created by 49488 on 2019/1/21.
 */

public class ClearBean {

    /**
     * data : {"cabinetNo":"6"}
     * msgType : CLEAR_USER_CABINET
     */

    private DataBean data;
    private String msgType;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public static class DataBean {
        /**
         * cabinetNo : 6
         */

        private String cabinetNo;

        public String getCabinetNo() {
            return cabinetNo;
        }

        public void setCabinetNo(String cabinetNo) {
            this.cabinetNo = cabinetNo;
        }
    }
}
