package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.support.StandardUnicastEventDispatcher;
import io.basc.framework.util.Selector;
import io.basc.framework.util.concurrent.TaskQueue;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTransactionEventDispatcher extends StandardUnicastEventDispatcher<ObjectEvent<Transaction>>
		implements TransactionEventDispatcher {

	public SimpleTransactionEventDispatcher() {
		super(Selector.roundRobin(), new TaskQueue());
	}
}
