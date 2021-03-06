package io.github.wcnnkh.interconnection.core;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import io.basc.framework.convert.annotation.JSON;
import io.basc.framework.lang.Nullable;
import io.basc.framework.orm.annotation.Entity;
import io.basc.framework.orm.annotation.PrimaryKey;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class TransactionRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	@Schema(description = "交易id", required = true)
	@NotEmpty
	@PrimaryKey
	private String transactionId;
	@JSON
	@Schema(description = "扩展数据")
	private Map<String, String> extendedData;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Nullable
	public Map<String, String> getExtendedData() {
		return extendedData;
	}

	public void setExtendedData(Map<String, String> extendedData) {
		this.extendedData = extendedData;
	}
}
