package io.github.wcnnkh.interconnection.core;

import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.UnicastEventDispatcher;

public interface TransactionEventDispatcher extends UnicastEventDispatcher<ObjectEvent<Transaction>> {
}
