package io.github.wcnnkh.interconnection.core;

import java.io.Serializable;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transactionId;
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
