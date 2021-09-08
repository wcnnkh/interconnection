package io.github.wcnnkh.interconnection.core;

import io.swagger.v3.oas.annotations.media.Schema;

public class TransactionUpdate extends Transaction {
	private static final long serialVersionUID = 1L;
	@Schema(description = "旧状态")
	private TransactionStatus oldStatus;

	public TransactionStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(TransactionStatus oldStatus) {
		this.oldStatus = oldStatus;
	}
}
