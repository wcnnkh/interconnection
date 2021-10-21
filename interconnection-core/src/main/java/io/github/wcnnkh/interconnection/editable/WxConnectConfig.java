package io.github.wcnnkh.interconnection.editable;

import io.basc.start.data.annotation.Editable;
import io.basc.start.tencent.wx.WxConnect;
import io.swagger.v3.oas.annotations.media.Schema;

@Editable(title = "微信授权配置")
public class WxConnectConfig extends WxConnect {
	private static final long serialVersionUID = 1L;
	@Schema(description = "配置id")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
