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
	@Schema(description = "授权类型 0是默认,1是扫码", example = "0", defaultValue = "0", required = true)
	private int type;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
