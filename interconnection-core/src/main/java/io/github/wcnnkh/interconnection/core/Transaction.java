package io.github.wcnnkh.interconnection.core;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "交易id")
	private String transactionId;
	@Schema(description = "交易状态", enumAsRef = true)
	private TransactionStatus status;

	public Transaction() {
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
}
