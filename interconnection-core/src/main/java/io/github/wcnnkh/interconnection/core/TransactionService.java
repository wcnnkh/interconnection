package io.github.wcnnkh.interconnection.core;

import io.basc.framework.context.result.Result;

public interface TransactionService {
	/**
	 * 创建一个交易
	 * 
	 * @return
	 */
	Transaction create();

	/**
	 * 确认一个交易
	 * 
	 * @param transactionId
	 * @return
	 */
	Result confirm(ConfirmRequest request);
	
	/**
	 * 取消一个交易
	 * @param request
	 * @return
	 */
	Result cancel(CancelRequest request);

	/**
	 * 查询一个交易
	 * 
	 * @param transactionId
	 * @return
	 */
	Transaction getTransaction(String transactionId);
}
