package org.brijframework.monitor.threads;

import org.brijframework.monitor.GlobelScope;
import org.brijframework.monitor.factories.GlobelFactroy;

public class GlobelThreadLocal extends ThreadLocal<GlobelScope> {
	@Override
	protected GlobelScope initialValue() {
		GlobelScope service = GlobelFactroy.factory().getService();
		GlobelFactroy.factory().count++;
		GlobelFactroy.factory().setObject(service.getId(), service);
		return service;
	}
	
	@Override
	public GlobelScope get() {
		return super.get();
	}
}