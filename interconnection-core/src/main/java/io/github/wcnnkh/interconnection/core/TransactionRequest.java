package io.github.wcnnkh.interconnection.core;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class TransactionRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	@Schema(description = "交易id", required = true)
	@NotEmpty
	private String transactionId;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
