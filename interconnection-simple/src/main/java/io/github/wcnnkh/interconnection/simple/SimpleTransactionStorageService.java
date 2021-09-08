package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.db.DB;
import io.basc.framework.env.Sys;
import io.basc.framework.json.JSONUtils;
import io.basc.framework.sql.SimpleSql;
import io.basc.framework.sql.Sql;
import io.basc.framework.sqlite.SQLiteDB;
import io.basc.framework.util.XTime;
import io.basc.framework.util.XUtils;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionStatus;
import io.github.wcnnkh.interconnection.core.TransactionStorageService;
import io.github.wcnnkh.interconnection.core.TransactionUpdate;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTransactionStorageService implements TransactionStorageService {
	private final DB db = new SQLiteDB(Sys.env.getWorkPath() + "/transaction.db");

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
	public boolean update(TransactionUpdate update) {
		Transaction transaction = getTransaction(update.getTransactionId());
		if (transaction == null) {
			return false;
		}

		Sql sql = new SimpleSql("update `transaction` set status=?, extendedData=? where transactionId=? and status=?",
				update.getStatus(),
				update.getExtendedData() == null ? null
						: JSONUtils.getJsonSupport().toJSONString(update.getExtendedData()),
				update.getTransactionId(), update.getOldStatus());
		return db.update(sql) > 0;
	}

}
