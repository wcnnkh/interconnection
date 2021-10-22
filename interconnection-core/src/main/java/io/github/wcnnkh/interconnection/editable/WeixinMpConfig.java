package io.github.wcnnkh.interconnection.editable;

import io.basc.framework.orm.annotation.PrimaryKey;
import io.basc.start.data.annotation.Editable;
import io.basc.start.data.annotation.Readonly;

import java.io.Serializable;

@Editable(title = "微信基础配置")
public class WeixinMpConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private String appid;
	private String appsecret;
	
	//TODO 以下两个token应该分表的，但为了方便所以使用了一张表，使用同一个表会存在并发更新的问题(会增加很多无用的请求)，但这里忽略了
	private String accessToken;
	@Readonly
	private Long accessTokenCreateTime;
	private String jsApiTicket;
	@Readonly
	private Long jsApiTicketCreateTime;

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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getAccessTokenCreateTime() {
		return accessTokenCreateTime;
	}

	public void setAccessTokenCreateTime(Long accessTokenCreateTime) {
		this.accessTokenCreateTime = accessTokenCreateTime;
	}

	public String getJsApiTicket() {
		return jsApiTicket;
	}

	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}

	public Long getJsApiTicketCreateTime() {
		return jsApiTicketCreateTime;
	}

	public void setJsApiTicketCreateTime(Long jsApiTicketCreateTime) {
		this.jsApiTicketCreateTime = jsApiTicketCreateTime;
	}
}
