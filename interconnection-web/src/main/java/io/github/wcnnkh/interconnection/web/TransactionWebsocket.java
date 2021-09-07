package io.github.wcnnkh.interconnection.web;

import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.basc.framework.websocket.adapter.standard.StandardContainerConfigurator;

@ServerEndpoint(value = "/transaction/{transactionId}", configurator = StandardContainerConfigurator.class)
public class TransactionWebsocket {
	
	public void onOpen(Session session, @PathParam("transactionId") String transactionId) {

	}
}
