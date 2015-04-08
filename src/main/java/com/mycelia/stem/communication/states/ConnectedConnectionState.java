package com.mycelia.stem.communication.states;

import com.mycelia.stem.communication.StemClientSession;
import com.mycelia.stem.communication.handlers.ComponentHandlerBase;

public class ConnectedConnectionState implements ConnectionState {

	private ComponentHandlerBase handler;
	
	@Override
	public void primeConnectionState(StemClientSession session) {
		this.handler = session.getComponentHandler();
	}
	
	@Override
	public void process() {
		handler.handleComponent();
	}

	@Override
	public ComponentHandlerBase getHandler() {
		return handler;
	}

	@Override
	public void setHandler(ComponentHandlerBase handler) {
		this.handler = handler;
	}
	
	public String toString() {
		return "CONNECTED STATE!!";
	}

}
