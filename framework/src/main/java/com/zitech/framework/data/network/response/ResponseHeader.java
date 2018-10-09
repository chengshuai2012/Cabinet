package com.zitech.framework.data.network.response;

/**
 * 响应头
 * 
 * @author Ludaiqian
 * 
 */
public class ResponseHeader {
	// 操作成功或者失败的代码,200代表成功
	private int status;
	// 操作失败的提示
	private String sign;
	// 接口业务代码
	private String datecode;
	//
	private String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getDatecode() {
		return datecode;
	}

	public void setDatecode(String datecode) {
		this.datecode = datecode;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
