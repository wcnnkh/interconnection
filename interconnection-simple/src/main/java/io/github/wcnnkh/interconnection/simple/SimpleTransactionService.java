package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.context.result.Result;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.util.XUtils;
import io.github.wcnnkh.interconnection.core.ConfirmRequest;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;
import io.github.wcnnkh.interconnection.core.TransactionService;
import io.github.wcnnkh.interconnection.core.TransactionStatus;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTransactionService implements TransactionService {
	private final ResultFactory resultFactory;
	private TransactionEventDispatcher transactionEventDispatcher;

	public SimpleTransactionService(ResultFactory resultFactory,
			TransactionEventDispatcher transactionEventDispatcher) {
		this.resultFactory = resultFactory;
		this.transactionEventDispatcher = transactionEventDispatcher;
	}

	@Override
	public Transaction create() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(XUtils.getUUID());
		transaction.setStatus(TransactionStatus.CREATED);

		transactionEventDispatcher.publishEvent(new ObjectEvent<Transaction>(transaction));
		return transaction;
	}

	@Override
	public Result confirm(ConfirmRequest request) {
		Transaction transaction = getTransaction(request.getTransactionId());
		if (transaction == null) {
			return resultFactory.error("交易不存在或已过期");
		}

		transaction.setStatus(TransactionStatus.CONFIRM);
		transactionEventDispatcher.publishEvent(new ObjectEvent<Transaction>(transaction));
		return resultFactory.success();
	}

	@Override
	public Transaction getTransaction(String transactionId) {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(transactionId);
		transaction.setStatus(TransactionStatus.EXIPRED);
		return transaction;
	}

}
