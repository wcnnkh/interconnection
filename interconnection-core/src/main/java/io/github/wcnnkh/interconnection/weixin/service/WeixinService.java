package io.github.wcnnkh.interconnection.weixin.service;

import io.basc.framework.context.result.DataResult;
import io.basc.start.tencent.wx.JsApiSignature;

public interface WeixinService {
	DataResult<String> getAccessToken(String appid);

	DataResult<String> getJsApiTicket(String appid);

	DataResult<JsApiSignature> getJsApiSignature(String appid, String url);
}
