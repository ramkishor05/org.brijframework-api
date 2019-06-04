package org.brijframework.model.factories;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.data.asm.ObjectData;
import org.brijframework.data.factories.asm.DataFactoryImpl;
import org.brijframework.factories.Factory;
import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.monitor.factories.RequestFactroy;
import org.brijframework.monitor.factories.SessionFactroy;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;

public class ModelFactory implements Factory{
 
	private ConcurrentHashMap<String, ObjectData> cache=new ConcurrentHashMap<>();
	private Container container;
	
	protected ModelFactory() {
	}

	protected static ModelFactory factory;

	@Assignable
	public static ModelFactory getFactory() {
		if (factory == null) {
			factory = new ModelFactory();
			factory.loadFactory();
		}
		return factory;
	}

	@Override
	public ModelFactory loadFactory() {
		return this;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public ConcurrentHashMap<String, ObjectData> getCache() {
		return cache;
	}

	@Override
	public ModelFactory clear() {
		getCache().clear();
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T getModel(String model) {
		ClassMeta owner= MetaFactoryImpl.getFactory().getMeta(model);
		Assertion.notNull(owner, "Model meta not found");
		String uniqueID=getUniqueID(owner);
		ObjectData dataInfo= DataFactoryImpl.getFactory().getData(uniqueID);
		if(dataInfo==null) {
			dataInfo=DataFactoryImpl.getFactory().register(uniqueID,owner);
			dataInfo.setScopeObject(buildScopeObject(uniqueID,owner));
		}
	    return (T) dataInfo.getScopeObject();
	}
	
	private Object buildScopeObject(String uniqueID, ClassMeta owner) {
		Object bean=InstanceUtil.getInstance(owner.getTarget(), owner.getConstructor().getValues());
		owner.getProperties().forEach((key,property)->{
			PropertyAccessorUtil.setProperty(bean, property.getTarget(), property.getValue());
		});
		return bean;
	}

	private String getUniqueID(ClassMeta metaSetup) {
		switch (metaSetup.getScope()) {
		case SINGLETON:
			return metaSetup.getId();
		case SESSION:
			return metaSetup.getId()+SessionFactroy.factory().currentService().getId();
		case REQUEST:
			return metaSetup.getId()+RequestFactroy.factory().currentService().getId();
		case PROTOTYPE:
			return metaSetup.getId()+RequestFactroy.factory().currentService().getId();
		default:
			return metaSetup.getId();
		}
	}
	
}
