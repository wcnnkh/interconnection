package io.github.wcnnkh.interconnection.core;


public interface TransactionStorageService {
	Transaction getTransaction(String transactionId);

	Transaction create();

	boolean update(TransactionUpdate update);
}
