package io.github.wcnnkh.interconnection.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.codec.support.URLCodec;
import io.basc.framework.mvc.message.annotation.QueryParams;
import io.basc.framework.mvc.model.ModelAndView;
import io.basc.framework.net.uri.UriUtils;
import io.basc.framework.util.StringUtils;
import io.basc.framework.web.ServerHttpRequest;
import io.basc.framework.web.ServerHttpResponse;
import io.basc.start.app.configure.AppConfigure;
import io.basc.start.data.DataService;
import io.github.wcnnkh.interconnection.editable.WxConnectConfig;
import io.github.wcnnkh.interconnection.web.dto.WxAuthorizeRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/weixin")
@Tag(name = "微信授权登录")
public class WeixinController {
	private final DataService dataService;
	@Autowired
	private AppConfigure appConfigure;

	public WeixinController(DataService dataService) {
		this.dataService = dataService;
	}

	@Path("/authorize")
	@GET
	public void authorize(@QueryParams WxAuthorizeRequest request,
			@Parameter(hidden = true) ServerHttpResponse response,
			@Parameter(hidden = true) ServerHttpRequest serverHttpRequest)
			throws IOException {
		WxConnectConfig config = dataService.getById(WxConnectConfig.class,
				request.getConnectId());
		if (config == null) {
			return;
		}

		if (StringUtils.isNotEmpty(request.getState())) {
			config.setState(request.getState());
		}

		config.setRedirectUri(StringUtils.cleanPath(appConfigure.getHost()
				+ serverHttpRequest.getContextPath()
				+ "/weixin/authorize/call/" + request.getConnectId()));
		response.sendRedirect(request.isQr() ? config.toQrconnectUrl() : config
				.toAuthorizeUrl());
	}

	@Path("/authorize/call/{connnectId}")
	@GET
	@Hidden
	public ModelAndView authorizeCall(@NotNull Integer connectId,
			@NotEmpty String code, String state) {
		WxConnectConfig config = dataService.getById(WxConnectConfig.class,
				connectId);
		if (config == null) {
			return null;
		}

		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("code", code);
		params.put("state", StringUtils.isEmpty(state) ? config.getState()
				: state);

		ModelAndView view = new ModelAndView("/ftl/wx-connect-call.ftl");
		view.put("url", UriUtils.appendQueryParams(config.getRedirectUri(),
				params, URLCodec.UTF_8));
		return view;
	}
}
