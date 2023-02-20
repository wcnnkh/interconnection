package io.github.wcnnkh.interconnection.core.support;

import java.util.HashMap;
import java.util.Map;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.context.transaction.DataResult;
import io.basc.framework.context.transaction.Result;
import io.basc.framework.context.transaction.ResultFactory;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.lang.Nullable;
import io.basc.framework.mapper.Copy;
import io.github.wcnnkh.interconnection.core.SyncRequest;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;
import io.github.wcnnkh.interconnection.core.TransactionService;
import io.github.wcnnkh.interconnection.core.TransactionStatus;
import io.github.wcnnkh.interconnection.core.TransactionStorageService;
import io.github.wcnnkh.interconnection.core.TransactionUpdate;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class DefaultTransactionService implements TransactionService {
	private final TransactionStorageService transactionStorageService;
	private final TransactionEventDispatcher transactionEventDispatcher;
	private final SyncProcessor syncProcessor;
	private final ResultFactory resultFactory;

	public DefaultTransactionService(ResultFactory resultFactory, TransactionStorageService transactionStorageService,
			TransactionEventDispatcher transactionEventDispatcher, @Nullable SyncProcessor syncProcessor) {
		this.transactionEventDispatcher = transactionEventDispatcher;
		this.transactionStorageService = transactionStorageService;
		this.syncProcessor = syncProcessor;
		this.resultFactory = resultFactory;
	}

	@Override
	public Transaction create() {
		return transactionStorageService.create();
	}

	@Override
	public Result sync(SyncRequest request) {
		Transaction transaction = transactionStorageService.getTransaction(request.getTransactionId());
		if (transaction == null) {
			return resultFactory.error("交易不存在");
		}

		if (!TransactionStatus.canUpdate(transaction.getStatus(), request.getStatus())) {
			return resultFactory.error("状态不合法");
		}

		Map<String, String> map = transaction.getExtendedData();
		if (map == null) {
			map = new HashMap<>();
		}

		if (request.getExtendedData() != null) {
			map.putAll(request.getExtendedData());
		}

		if (syncProcessor != null) {
			DataResult<Map<String, String>> syncResult = syncProcessor.sync(request);
			if (syncResult.isError()) {
				return syncResult;
			}

			if (syncResult.getData() != null) {
				map.putAll(syncResult.getData());
			}
		}

		transaction.setExtendedData(map);

		TransactionUpdate update = new TransactionUpdate();
		update.setOldStatus(transaction.getStatus());

		transaction.setStatus(request.getStatus());
		Copy.copy(transaction, update);

		if (!transactionStorageService.update(update)) {
			return resultFactory.error("操作失败");
		}
		transactionEventDispatcher.publishEvent(new ObjectEvent<>(transaction));
		return resultFactory.success();
	}

	@Override
	public Transaction getTransaction(String transactionId) {
		return transactionStorageService.getTransaction(transactionId);
	}

}
