package org.brijframework.monitor.factories;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.monitor.GlobelScope;
import org.brijframework.monitor.threads.GlobelThreadLocal;
import org.brijframework.util.reflect.InstanceUtil;

public class GlobelFactroy{
	
	public int count;
	private static GlobelFactroy factory;
	private GlobelThreadLocal thread;
	private GlobelScope service;
	private static ConcurrentHashMap<Object, GlobelScope> container = new ConcurrentHashMap<>();

	public static GlobelFactroy factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(GlobelFactroy.class);
		}
		return factory;
	}

	public GlobelScope currentService() {
		if (this.thread == null) {
			registerService(new GlobelScope());
		}
		return thread.get();
	}

	public GlobelFactroy registerService(GlobelScope service) {
		this.service = service;
		this.thread = new GlobelThreadLocal();
		return factory;
	}

	public GlobelScope getService() {
		return this.service;
	}

	public void setObject(Object key, GlobelScope service) {
		container.put(key, service);
	}

}