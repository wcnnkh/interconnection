package io.github.wcnnkh.interconnection.editable;

import io.basc.start.data.annotation.Editable;

import java.io.Serializable;

@Editable(title = "微信基础配置")
public class WeixinMpConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private String appid;
	private String appsecret;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

}
