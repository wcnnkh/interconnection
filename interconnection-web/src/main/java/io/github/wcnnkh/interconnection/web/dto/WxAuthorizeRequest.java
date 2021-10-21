package io.github.wcnnkh.interconnection.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class WxAuthorizeRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "连接的配置id, 在后台管理系统查看", required = true)
	@NotNull
	private Integer connectId;
	@Schema(description = "状态,会回传")
	private String state;
	@Schema(description = "是否是扫码授权", example = "false", defaultValue = "false", required = true)
	private boolean qr;

	public Integer getConnectId() {
		return connectId;
	}

	public void setConnectId(Integer connectId) {
		this.connectId = connectId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isQr() {
		return qr;
	}

	public void setQr(boolean qr) {
		this.qr = qr;
	}
}
