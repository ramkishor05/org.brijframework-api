package org.brijframework.monitor.threads;

import org.brijframework.monitor.SessionScope;
import org.brijframework.monitor.factories.SessionFactroy;

public class SessionThreadLocal extends ThreadLocal<SessionScope> {
	@Override
	protected SessionScope initialValue() {
		SessionScope service = SessionFactroy.factory().getService();
		SessionFactroy.factory().count++;
		SessionFactroy.factory().setObject(service.getId(), service);
		return service;
	}
	
	@Override
	public SessionScope get() {
		return super.get();
	}
}