package io.github.wcnnkh.interconnection.weixin.service;

import io.basc.framework.context.transaction.DataResult;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureRequest;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureResponse;

public interface WeixinService {
	DataResult<String> getAccessToken(String appid);

	DataResult<String> getJsApiTicket(String appid);

	DataResult<JsApiSignatureResponse> getJsApiSignature(JsApiSignatureRequest request);
}
