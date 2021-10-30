package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.beans.annotation.Service;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.mapper.Copy;
import io.basc.framework.oauth2.AccessToken;
import io.basc.framework.security.Token;
import io.basc.framework.util.RandomUtils;
import io.basc.framework.util.StringUtils;
import io.basc.start.data.DataService;
import io.basc.start.tencent.wx.JsApiSignature;
import io.basc.start.tencent.wx.WeiXinUtils;
import io.github.wcnnkh.interconnection.weixin.WeixinMpConfig;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureRequest;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureResponse;
import io.github.wcnnkh.interconnection.weixin.service.WeixinService;

@Service
public class WeixinServiceImpl implements WeixinService {
	private final DataService dataService;
	@Autowired
	private ResultFactory resultFactory;

	public WeixinServiceImpl(DataService dataService) {
		this.dataService = dataService;
	}

	@Override
	public DataResult<String> getAccessToken(String appid) {
		WeixinMpConfig config = dataService
				.getById(WeixinMpConfig.class, appid);
		if (config == null) {
			return resultFactory.error("appid不存在");
		}

		Long createTime = config.getAccessTokenCreateTime();
		if (StringUtils.isEmpty(config.getAccessToken())
				|| createTime == null
				|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
			// 已过期
			synchronized (this) {
				config = dataService.getById(WeixinMpConfig.class, appid);
				if (config == null) {
					return resultFactory.error("appid不存在");
				}

				createTime = config.getAccessTokenCreateTime();
				if (StringUtils.isEmpty(config.getAccessToken())
						|| createTime == null
						|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
					AccessToken accessToken = WeiXinUtils.getAccessToken(
							config.getAppid(), config.getAppsecret());
					config.setAccessToken(accessToken.getToken().getToken());
					config.setAccessTokenCreateTime(System.currentTimeMillis());
					dataService.update(config);
				}
			}
		}
		return resultFactory.success(config.getAccessToken());
	}

	@Override
	public DataResult<String> getJsApiTicket(String appid) {
		WeixinMpConfig config = dataService
				.getById(WeixinMpConfig.class, appid);
		if (config == null) {
			return resultFactory.error("appid不存在");
		}

		Long createTime = config.getJsApiTicketCreateTime();
		if (StringUtils.isEmpty(config.getJsApiTicket())
				|| createTime == null
				|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
			// 已过期
			synchronized (this) {
				config = dataService.getById(WeixinMpConfig.class, appid);
				if (config == null) {
					return resultFactory.error("appid不存在");
				}

				DataResult<String> accessTokenResponse = getAccessToken(appid);
				if (accessTokenResponse.isError()) {
					return accessTokenResponse;
				}

				createTime = config.getJsApiTicketCreateTime();
				if (StringUtils.isEmpty(config.getJsApiTicket())
						|| createTime == null
						|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
					Token token = WeiXinUtils
							.getJsApiTicket(accessTokenResponse.getData());
					config.setJsApiTicket(token.getToken());
					config.setJsApiTicketCreateTime(System.currentTimeMillis());
					dataService.update(config);
				}
			}
		}
		return resultFactory.success(config.getJsApiTicket());
	}

	@Override
	public DataResult<JsApiSignatureResponse> getJsApiSignature(JsApiSignatureRequest request) {
		DataResult<String> ticketResponse = getJsApiTicket(request.getAppid());
		if (ticketResponse.isError()) {
			return ticketResponse.dataResult();
		}
		
		JsApiSignatureResponse response = new JsApiSignatureResponse();
		Copy.copy(request, response);
		if(StringUtils.isEmpty(response.getNonceStr())){
			response.setNonceStr(RandomUtils.getRandomStr(10));
		}
		
		if(response.getTimestamp() != null){
			response.setTimestamp(System.currentTimeMillis()/1000);
		}

		JsApiSignature jsApiSignature = new JsApiSignature(response.getNonceStr(), ticketResponse.getData(), response.getTimestamp(), response.getUrl());
		response.setSignature(jsApiSignature.getSignature());
		return resultFactory.success(response);
	}

}
