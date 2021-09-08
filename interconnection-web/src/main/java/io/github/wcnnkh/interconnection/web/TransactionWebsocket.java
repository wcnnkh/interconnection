package io.github.wcnnkh.interconnection.web;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.event.EventListener;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.json.JSONUtils;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.websocket.adapter.standard.StandardContainerConfigurator;
import io.basc.framework.websocket.adapter.standard.StandardSessionManager;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionEventDispatcher;
import io.github.wcnnkh.interconnection.core.TransactionService;

@ServerEndpoint(value = "/transaction/{transactionId}", configurator = StandardContainerConfigurator.class)
public class TransactionWebsocket implements EventListener<ObjectEvent<Transaction>> {
	private static Logger logger = LoggerFactory.getLogger(TransactionWebsocket.class);

	private StandardSessionManager<String> manager = new StandardSessionManager<>("transactionId");

	@Autowired
	private TransactionEventDispatcher transactionEventDispatcher;
	@Autowired
	private TransactionService transactionService;

	@OnOpen
	public void onOpen(Session session, @PathParam("transactionId") String transactionId) {
		manager.putIfAbsent(transactionId, session);
	}

	@OnClose
	public void onClose(Session session) {
		manager.remove(session);
	}

	@Override
	public void onEvent(ObjectEvent<Transaction> event) {
		manager.getSessions(event.getSource().getTransactionId()).forEach((session) -> {
			try {
				session.getBasicRemote().sendText(JSONUtils.getJsonSupport().toJSONString(event.getSource()));
			} catch (Exception e) {
				logger.error(e, "发送消息异常:{}", JSONUtils.getJsonSupport().toJSONString(event.getSource()));
			}
		});
	}
}
