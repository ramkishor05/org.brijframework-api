package org.brijframework.model.setup;

import org.brijframework.support.enums.Wiring;

public class RelPtpMetaSetup extends PropertyMetaSetup {

	private Class<?> mappedTo;

	private String mappedBy;

	private Wiring referred;

	public Class<?> getMappedTo() {
		return mappedTo;
	}

	public void setMappedTo(Class<?> mappedTo) {
		this.mappedTo = mappedTo;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	public Wiring getReferred() {
		return referred;
	}

	public void setReferred(Wiring referred) {
		this.referred = referred;
	}
	
}
