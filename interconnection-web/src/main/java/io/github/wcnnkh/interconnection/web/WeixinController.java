package io.github.wcnnkh.interconnection.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.basc.framework.mvc.message.annotation.QueryParams;
import io.basc.framework.util.StringUtils;
import io.basc.framework.web.ServerHttpResponse;
import io.basc.start.data.DataService;
import io.github.wcnnkh.interconnection.editable.WxConnectConfig;
import io.github.wcnnkh.interconnection.web.dto.WxAuthorizeRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/weixin")
@Tag(name = "测试")
public class WeixinController {
	private final DataService dataService;
	
	public WeixinController(DataService dataService) {
		this.dataService = dataService;
	}
	
	@Path("/authorize")
	@GET
	public void authorize(@QueryParams WxAuthorizeRequest request, @Parameter(hidden = true) ServerHttpResponse response) {
		WxConnectConfig config = dataService.getById(WxConnectConfig.class, request.getConnectId());
		if(config == null) {
			return ;
		}

		if(StringUtils.isNotEmpty(request.getState())) {
			config.setState(request.getState());
		}
	}
	
	@Path("/authorize/call/{connnectId}")
	@GET
	@Hidden
	public void authorizeCall(String connectId, String code, String state) {
		
	}
}
