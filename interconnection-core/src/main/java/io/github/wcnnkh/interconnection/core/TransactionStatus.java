package io.github.wcnnkh.interconnection.core;


public enum TransactionStatus {
	CREATED("已创建"), 
	EXIPRED("已过期"),
	CONFIRMED("已确认"),
	CANCELLED("已取消");

	private final String desc;

	private TransactionStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
