package io.github.wcnnkh.interconnection.weixin;

import javax.validation.constraints.NotEmpty;

import io.basc.framework.orm.annotation.AutoIncrement;
import io.basc.framework.orm.annotation.Entity;
import io.basc.framework.orm.annotation.PrimaryKey;
import io.basc.start.editable.annotation.Editable;
import io.basc.start.tencent.wx.open.WxConnect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Editable
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(comment = "微信授权配置")
public class WxConnectConfig extends WxConnect {
	private static final long serialVersionUID = 1L;
	@Schema(description = "配置id", required = true)
	@PrimaryKey
	@NotEmpty
	@AutoIncrement
	private Integer id;
}
