package io.github.wcnnkh.interconnection.core;

public enum TransactionStatus {
	CREATED("已创建"), 
	EXIPRED("已过期"),
	CONFIRM("已确认"),;

	private final String desc;

	private TransactionStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
