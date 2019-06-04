package org.brijframework.model.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.context.Context;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class ModelContext implements Context{

	Context context;
	
	ConcurrentHashMap<Object, Container> containers=new ConcurrentHashMap<Object, Container>();
	
	@Override
	public void initialize(Context context) {
		this.context=context;
	}

	@Override
	public void startup() {
		System.err.println("ModelContext loading start.");
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(Container.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					loading(cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(Container.class.isAssignableFrom(cls) && !cls.isInterface() && cls.getModifiers() != Modifier.ABSTRACT) {
					loading(cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("ModelContext loading done.");
	}

	private void loading(Class<?>cls) {
		boolean called=false;
		for(Method method:MethodUtil.getAllMethod(cls)) {
			if(method.isAnnotationPresent(Assignable.class)) {
				try {
					System.err.println("Container :"+cls.getName());
					Container container=(Container) method.invoke(null);
					getContainers().put(cls.getSimpleName(), container);
					called=true;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if(!called) {
			try {
				System.err.println("Container :"+cls.getName());
				Container container=(Container) cls.newInstance();
				container.loadContainer();
				getContainers().put(cls.getSimpleName(), container);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void destory() {
	}

	@Override
	public Context getParent() {
		return this.context;
	}

	@Override
	public ConcurrentHashMap<Object, Container> getContainers() {
		return containers;
	}

}
