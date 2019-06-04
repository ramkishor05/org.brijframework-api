package org.brijframework.model.setup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.brijframework.meta.setup.ClassMetaSetup;
import org.brijframework.meta.setup.FieldMetaSetup;

public class ModelMetaSetup implements ClassMetaSetup {
	private String id;
	private String name;
	private String target;
	private String access;
	private String scope;
	private String type;
		
	private Map<String, FieldMetaSetup> properties;
	

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	@Override
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public Map<String, FieldMetaSetup> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, FieldMetaSetup> properties) {
		this.properties = properties;
	}

	public void addProperty(FieldMetaSetup setup) {
		Objects.requireNonNull(setup.getId(), "Property id should not empty.");
		if (this.getProperties() == null) {
			this.setProperties(new HashMap<>());
		}
		this.getProperties().put(setup.getId(), setup);
	}

	public FieldMetaSetup getProperty(String setup) {
		if (this.getProperties() == null) {
			return null;
		}
		return this.getProperties().get(setup);
	}

	@Override
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
