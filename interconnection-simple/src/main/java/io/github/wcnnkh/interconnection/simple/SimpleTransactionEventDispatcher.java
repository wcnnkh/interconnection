package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.support.SimpleAsyncEventDispatcher;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTransactionEventDispatcher extends SimpleAsyncEventDispatcher<ObjectEvent<Transaction>>
		implements TransactionEventDispatcher {
}
