package io.github.wcnnkh.interconnection.core;

import io.basc.framework.context.transaction.Result;

public interface TransactionService {
	/**
	 * 创建一个交易
	 * 
	 * @return
	 */
	Transaction create();

	/**
	 * 同步交易
	 * 
	 * @param request
	 * @return
	 */
	Result sync(SyncRequest request);

	/**
	 * 查询一个交易
	 * 
	 * @param transactionId
	 * @return
	 */
	Transaction getTransaction(String transactionId);
}
