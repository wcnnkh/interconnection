package io.github.wcnnkh.interconnection.weixin;

import io.basc.framework.orm.annotation.AutoIncrement;
import io.basc.framework.orm.annotation.PrimaryKey;
import io.basc.start.data.annotation.Editable;
import io.basc.start.tencent.wx.open.WxConnect;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

@Editable(title = "微信授权配置")
public class WxConnectConfig extends WxConnect {
	private static final long serialVersionUID = 1L;
	@Schema(description = "配置id", required = true)
	@PrimaryKey
	@NotEmpty
	@AutoIncrement
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
