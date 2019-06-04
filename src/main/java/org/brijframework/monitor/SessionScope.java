package org.brijframework.monitor;

import org.brijframework.util.runtime.RandomUtil;

public class SessionScope{
	private String id;
	private String device;

	public SessionScope() {
		this.id = RandomUtil.genRandomUUID();
	}
	
	public String getId() {
		return id;
	}
	
	public void setDevice(String device) {
		this.device = device;
	}
	
	public String getDevice() {
		return device;
	}
	
}



