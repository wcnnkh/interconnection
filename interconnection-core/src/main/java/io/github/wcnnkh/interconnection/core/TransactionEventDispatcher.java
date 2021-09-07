package io.github.wcnnkh.interconnection.core;

import io.basc.framework.event.EventDispatcher;
import io.basc.framework.event.ObjectEvent;

public interface TransactionEventDispatcher extends EventDispatcher<ObjectEvent<Transaction>> {
}
