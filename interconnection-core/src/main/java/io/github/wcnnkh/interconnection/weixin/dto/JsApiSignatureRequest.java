package io.github.wcnnkh.interconnection.weixin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

@Schema(description = "生成微信js api signaure")
public class JsApiSignatureRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "appid", required = true)
	@NotEmpty
	private String appid;
	@Schema(description = "需要进行签名的url", required = true)
	@NotEmpty
	private String url;
	@Schema(description = "这个随机字符串的S在前端是大写的，可是在签名的时候是小写的")
	private String nonceStr;
	@Schema(description = "时间(秒)")
	private Long timestamp;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
