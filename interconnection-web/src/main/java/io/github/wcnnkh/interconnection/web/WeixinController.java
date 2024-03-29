package io.github.wcnnkh.interconnection.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.basc.framework.codec.support.URLCodec;
import io.basc.framework.context.ioc.annotation.Autowired;
import io.basc.framework.context.ioc.annotation.Value;
import io.basc.framework.context.transaction.DataResult;
import io.basc.framework.context.transaction.ResultFactory;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.mapper.Fields;
import io.basc.framework.mvc.annotation.Controller;
import io.basc.framework.net.uri.UriUtils;
import io.basc.framework.orm.repository.RepositoryTemplate;
import io.basc.framework.util.StringUtils;
import io.basc.framework.web.ServerHttpRequest;
import io.basc.framework.web.ServerHttpResponse;
import io.basc.framework.web.message.annotation.QueryParams;
import io.basc.framework.web.message.model.ModelAndView;
import io.basc.start.tencent.wx.api.UserAccessToken;
import io.basc.start.tencent.wx.api.Userinfo;
import io.basc.start.tencent.wx.api.WeiXinOffiaccount;
import io.basc.start.tencent.wx.open.Scope;
import io.github.wcnnkh.interconnection.web.dto.WxAuthorizeRequest;
import io.github.wcnnkh.interconnection.weixin.WeixinMpConfig;
import io.github.wcnnkh.interconnection.weixin.WxConnectConfig;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureRequest;
import io.github.wcnnkh.interconnection.weixin.dto.JsApiSignatureResponse;
import io.github.wcnnkh.interconnection.weixin.service.WeixinService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Path("/weixin")
@Tag(name = "微信授权")
public class WeixinController {
	private static Logger logger = LoggerFactory.getLogger(WeixinController.class);
	private final RepositoryTemplate dataService;
	@Autowired
	private WeixinService weixinService;
	@Value("qrcode.host")
	private String host;
	@Autowired
	private ResultFactory resultFactory;

	public WeixinController(RepositoryTemplate dataService) {
		this.dataService = dataService;
	}

	@Path("/authorize")
	@GET
	public void authorize(@QueryParams WxAuthorizeRequest request,
			@Parameter(hidden = true) ServerHttpResponse response,
			@Parameter(hidden = true) ServerHttpRequest serverHttpRequest) throws IOException {
		WxConnectConfig config = dataService.getById(WxConnectConfig.class, request.getConnectId());
		if (config == null) {
			return;
		}

		if (StringUtils.isNotEmpty(request.getState())) {
			config.setState(request.getState());
		}

		config.setRedirectUri((host == null ? "" : host) + StringUtils
				.cleanPath(serverHttpRequest.getContextPath() + "/weixin/authorize/code/" + request.getConnectId()));
		if (logger.isDebugEnabled()) {
			logger.debug("connect [{}] redirect [{}]", request.getConnectId(), config.getRedirectUri());
		}
		response.sendRedirect(request.isQr() ? config.toQrconnectUrl() : config.toAuthorizeUrl());
	}

	@Path("/authorize/code/{connectId}")
	@GET
	@Hidden
	public ModelAndView authorizeCall(@NotNull Integer connectId, @NotEmpty String code, String state) {
		WxConnectConfig config = dataService.getById(WxConnectConfig.class, connectId);
		if (config == null) {
			return null;
		}

		Map<String, Object> params = new HashMap<String, Object>(8);
		params.put("code", code);
		params.put("state", StringUtils.isEmpty(state) ? config.getState() : state);

		WeixinMpConfig weixinMpConfig = dataService.getById(WeixinMpConfig.class, config.getAppid());
		if (weixinMpConfig != null) {
			WeiXinOffiaccount weiXinOffiaccount = new WeiXinOffiaccount(weixinMpConfig.getAppid(),
					weixinMpConfig.getAppsecret());
			UserAccessToken userToken = weiXinOffiaccount.getUserAccessToken(code);
			if (userToken != null) {
				params.put("openid", userToken.getOpenid());
				if (!Scope.BASE.getValue().equals(config.getScope())) {
					Userinfo userinfo = weiXinOffiaccount.getUserinfo(userToken.getOpenid(),
							userToken.getToken().getToken());
					if (userinfo != null) {
						params.putAll(Fields.getFields(Userinfo.class).ignoreStatic().all().getValueMap(userinfo));
					}
				}
			}
		}

		ModelAndView view = new ModelAndView("/ftl/wx-connect-call.ftl");
		String url = UriUtils.appendQueryParams(config.getRedirectUri(), params, URLCodec.UTF_8);
		view.put("url", url);
		if (logger.isDebugEnabled()) {
			logger.debug("connect [{}] to [{}]", connectId, url);
		}
		return view;
	}

	@Path("/signature")
	@GET
	@Operation(description = "获取js api signature")
	public DataResult<JsApiSignatureResponse> getJsApiSignature(@QueryParams JsApiSignatureRequest request) {
		return weixinService.getJsApiSignature(request);
	}
}
