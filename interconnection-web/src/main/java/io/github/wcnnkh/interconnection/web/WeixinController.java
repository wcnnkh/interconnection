package io.github.wcnnkh.interconnection.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.github.wcnnkh.interconnection.core.Transaction;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/weixin")
@Tag(name = "微信相关接口")
public class WeixinController {
	@Path("/authorize")
	@GET
	public void authorize(Transaction transaction) {
		
	}
}
