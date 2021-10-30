package io.github.wcnnkh.interconnection.weixin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "微信生成js签名返回")
public class JsApiSignatureResponse extends JsApiSignatureRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "签名")
	private String signature;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
