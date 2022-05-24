package io.github.wcnnkh.interconnection.weixin;

import java.io.Serializable;

import io.basc.framework.lang.Nullable;
import io.basc.framework.orm.annotation.Entity;
import io.basc.framework.orm.annotation.PrimaryKey;
import io.basc.start.editable.annotation.Editable;
import lombok.Data;

@Editable
@Data
@Entity(comment = "微信基础配置")
public class WeixinMpConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private String appid;
	@Nullable(false)
	private String appsecret;
	
	//TODO 以下两个token应该分表的，但为了方便所以使用了一张表，使用同一个表会存在并发更新的问题(会增加很多无用的请求)，但这里忽略了
	private String accessToken;
	@Editable(readonly = true)
	private Long accessTokenCreateTime;
	private String jsApiTicket;
	@Editable(readonly = true)
	private Long jsApiTicketCreateTime;
}
