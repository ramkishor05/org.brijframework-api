package org.brijframework.monitor.factories;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.monitor.SessionScope;
import org.brijframework.monitor.threads.SessionThreadLocal;
import org.brijframework.util.reflect.InstanceUtil;

public class SessionFactroy{
	
	public int count;
	private static SessionFactroy factory;
	private SessionThreadLocal thread;
	private SessionScope service;
	private static ConcurrentHashMap<Object, SessionScope> container = new ConcurrentHashMap<>();

	public static SessionFactroy factory() {
		if (factory == null) {
			factory = InstanceUtil.getSingletonInstance(SessionFactroy.class);
		}
		return factory;
	}

	public SessionScope currentService() {
		if (this.thread == null) {
			registerService(new SessionScope());
		}
		return thread.get();
	}

	public SessionFactroy registerService(SessionScope service) {
		this.service = service;
		this.thread = new SessionThreadLocal();
		return factory;
	}

	public SessionScope getService() {
		return this.service;
	}

	public void setObject(Object key, SessionScope service) {
		container.put(key, service);
	}

}