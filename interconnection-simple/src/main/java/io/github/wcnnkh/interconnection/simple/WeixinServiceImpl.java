package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Service;
import io.basc.framework.context.ioc.annotation.Autowired;
import io.basc.framework.context.transaction.DataResult;
import io.basc.framework.context.transaction.ResultFactory;
import io.basc.framework.mapper.Copy;
import io.basc.framework.orm.repository.RepositoryTemplate;
import io.basc.framework.security.Token;
import io.basc.framework.util.RandomUtils;
import io.basc.framework.util.StringUtils;
import io.basc.start.tencent.wx.api.JsApiSignature;
import io.basc.start.tencent.wx.api.WeiXinApi;
import io.basc.start.tencent.wx.api.WeiXinOffiaccount;
import io.github.wcnnkh.interconnection.weixin.WeixinMpConfig;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureRequest;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureResponse;
import io.github.wcnnkh.interconnection.weixin.service.WeixinService;

@Service
public class WeixinServiceImpl implements WeixinService {
	private final RepositoryTemplate dataService;
	@Autowired
	private ResultFactory resultFactory;

	public WeixinServiceImpl(RepositoryTemplate dataService) {
		this.dataService = dataService;
	}

	@Override
	public DataResult<String> getAccessToken(String appid) {
		WeixinMpConfig config = dataService.getById(WeixinMpConfig.class, appid);
		if (config == null) {
			return resultFactory.error("appid不存在");
		}

		Long createTime = config.getAccessTokenCreateTime();
		if (StringUtils.isEmpty(config.getAccessToken()) || createTime == null
				|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
			// 已过期
			synchronized (this) {
				config = dataService.getById(WeixinMpConfig.class, appid);
				if (config == null) {
					return resultFactory.error("appid不存在");
				}

				createTime = config.getAccessTokenCreateTime();
				if (StringUtils.isEmpty(config.getAccessToken()) || createTime == null
						|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
					WeiXinApi weixinApi = new WeiXinApi(config.getAppid(), config.getAppsecret());
					Token token = weixinApi.getClientCredential(false);
					config.setAccessToken(token.getToken());
					config.setAccessTokenCreateTime(token.getCreateTime());
					dataService.update(config);
				}
			}
		}
		return resultFactory.success(config.getAccessToken());
	}

	@Override
	public DataResult<String> getJsApiTicket(String appid) {
		WeixinMpConfig config = dataService.getById(WeixinMpConfig.class, appid);
		if (config == null) {
			return resultFactory.error("appid不存在");
		}

		Long createTime = config.getJsApiTicketCreateTime();
		if (StringUtils.isEmpty(config.getJsApiTicket()) || createTime == null
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
				if (StringUtils.isEmpty(config.getJsApiTicket()) || createTime == null
						|| (System.currentTimeMillis() - createTime) > (7200 - 60) * 1000) {
					WeiXinOffiaccount weixinApi = new WeiXinOffiaccount(config.getAppid(), config.getAppsecret());
					Token token = weixinApi.getJsapiTicket(accessTokenResponse.getData());
					config = dataService.getById(WeixinMpConfig.class, appid);
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
			return ticketResponse.toReturn();
		}

		JsApiSignatureResponse response = new JsApiSignatureResponse();
		Copy.copy(request, response);
		if (StringUtils.isEmpty(response.getNonceStr())) {
			response.setNonceStr(RandomUtils.randomString(10));
		}

		if (response.getTimestamp() == null) {
			response.setTimestamp(System.currentTimeMillis() / 1000);
		}

		JsApiSignature jsApiSignature = new JsApiSignature(response.getNonceStr(), ticketResponse.getData(),
				response.getTimestamp(), response.getUrl());
		response.setSignature(jsApiSignature.getSignature());
		return resultFactory.success(response);
	}

}
