package org.brijframework.monitor.threads;

import org.brijframework.monitor.RequestScope;
import org.brijframework.monitor.factories.RequestFactroy;

public class RequestThreadLocal extends ThreadLocal<RequestScope> {
	@Override
	protected RequestScope initialValue() {
		RequestScope service = RequestFactroy.factory().getService();
		RequestFactroy.factory().count++;
		RequestFactroy.factory().setObject(service.getId(), service);
		return service;
	}
	
	@Override
	public RequestScope get() {
		return super.get();
	}
}