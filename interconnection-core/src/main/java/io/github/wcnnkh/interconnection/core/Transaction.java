package io.github.wcnnkh.interconnection.core;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Transaction extends TransactionRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "交易状态", required = true)
	@NotNull
	private TransactionStatus status;
	@Schema(description = "过期时间， 单位：毫秒", example = "60000", required = true)
	private long expiryTime;
	@Schema(description = "创建时间戳, 单位：毫秒", example = "1111213213113", required = true)
	private long cts;

	public Transaction() {
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}
}
