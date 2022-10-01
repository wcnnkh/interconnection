package io.github.wcnnkh.interconnection.web;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.basc.framework.context.ioc.annotation.Autowired;
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

	private TransactionEventDispatcher transactionEventDispatcher;
	@Autowired
	private TransactionService transactionService;
	
	public TransactionWebsocket(TransactionEventDispatcher transactionEventDispatcher){
		this.transactionEventDispatcher = transactionEventDispatcher;
		this.transactionEventDispatcher.registerListener(this);
	}

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
			String message = JSONUtils.getJsonSupport().toJSONString(event.getSource());
			try {
				if(logger.isDebugEnabled()) {
					logger.debug("Send [{}] message: {}", manager.getGroup(session), message);
				}
				
				session.getBasicRemote().sendText(message);
			} catch (Exception e) {
				logger.error(e, "发送[{}]消息异常:{}", manager.getGroup(session), message);
			}
		});
	}
}
