package io.github.wcnnkh.interconnection.core;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class SyncRequest extends TransactionRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "交易状态", required = true, enumAsRef = true)
	@NotNull(message = "交易状态不能为空")
	private TransactionStatus status;

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
}
