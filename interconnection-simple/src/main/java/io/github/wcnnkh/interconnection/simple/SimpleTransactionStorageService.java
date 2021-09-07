package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.db.DB;
import io.basc.framework.env.Sys;
import io.basc.framework.sqlite.SQLiteDB;
import io.basc.framework.util.XTime;
import io.basc.framework.util.XUtils;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionStatus;
import io.github.wcnnkh.interconnection.core.TransactionStatusUpdate;
import io.github.wcnnkh.interconnection.core.TransactionStorageService;

public class SimpleTransactionStorageService implements
		TransactionStorageService {
	private final DB db = new SQLiteDB(Sys.env.getWorkPath()
			+ "/transaction.db");

	public SimpleTransactionStorageService() {
		db.createTable(Transaction.class);
	}

	@Override
	public Transaction getTransaction(String transactionId) {
		return db.getById(Transaction.class, transactionId);
	}

	@Override
	public Transaction create() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(XUtils.getUUID());
		transaction.setCts(System.currentTimeMillis());
		transaction.setExpiryTime(XTime.ONE_MINUTE);
		transaction.setStatus(TransactionStatus.CREATED);
		db.save(transaction);
		return transaction;
	}

	@Override
	public boolean updateStatus(TransactionStatusUpdate update) {
		Transaction transaction = getTransaction(update.getTransactionId());
		if (transaction == null) {
			return false;
		}
		//TODO 未处理并发
		transaction.setStatus(update.getStatus());
		db.update(transaction);
		return true;
	}

}
