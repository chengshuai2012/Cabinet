package com.zitech.framework.data.network.exception;


public class ApiException extends RuntimeException {

    public static final int INSUFFICIENT_BALANCE=100;//账户余额不足
    public static final int MOMENT_NOT_EXIST=101;//动态不存在
    public static final int HOT_DATE_LACK_PERMISSION = 124;//缺少权限加入热度过高的邀约旅行
    public static final int BLACK_LIST = 126;//拉黑
    public final int errorCode;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

