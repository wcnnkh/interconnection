package io.github.wcnnkh.interconnection.core.support;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.context.result.Result;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.github.wcnnkh.interconnection.core.CancelRequest;
import io.github.wcnnkh.interconnection.core.ConfirmRequest;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;
import io.github.wcnnkh.interconnection.core.TransactionService;
import io.github.wcnnkh.interconnection.core.TransactionStorageService;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class DefaultTransactionService implements TransactionService {
	private final TransactionStorageService transactionStorageService;
	private final TransactionEventDispatcher transactionEventDispatcher;

	public DefaultTransactionService(
			TransactionStorageService transactionStorageService,
			TransactionEventDispatcher transactionEventDispatcher) {
		this.transactionEventDispatcher = transactionEventDispatcher;
		this.transactionStorageService = transactionStorageService;
	}

	@Override
	public Transaction create() {
		return transactionStorageService.create();
	}

	@Override
	public Result confirm(ConfirmRequest request) {
		return null;
	}

	@Override
	public Result cancel(CancelRequest request) {
		return null;
	}

	@Override
	public Transaction getTransaction(String transactionId) {
		return transactionStorageService.getTransaction(transactionId);
	}

}
