package io.github.wcnnkh.interconnection.core;

import java.io.Serializable;

public class ConfirmRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transactionId;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
