package org.brijframework.monitor;

import org.brijframework.util.runtime.RandomUtil;

public class GlobelScope{
	private String id;
	private String device;

	public GlobelScope() {
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



