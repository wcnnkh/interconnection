package io.github.wcnnkh.interconnection.core;

import io.swagger.v3.oas.annotations.media.Schema;

public enum TransactionStatus {
	@Schema(description = "已创建")
	CREATED("已创建", 0), 
	EXIPRED("已过期", 2), 
	BEFORE_CONFIRM("确认前", 1), 
	CONFIRMED("已确认", 2), 
	BEFORE_CANCEL("取消前", 1),
	CANCELLED("已取消", 2);

	private final String desc;

	// 状态等级，只能从低等级变成高等级
	private final int level;

	private TransactionStatus(String desc, int level) {
		this.desc = desc;
		this.level = level;
	}

	public String getDesc() {
		return desc;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * 是否可以进行状态更新
	 * 
	 * @param oldStatus
	 * @param status
	 * @return
	 */
	public static boolean canUpdate(TransactionStatus oldStatus, TransactionStatus status) {
		return oldStatus.level < status.level;
	}
}
