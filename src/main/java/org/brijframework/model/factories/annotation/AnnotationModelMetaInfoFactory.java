package org.brijframework.model.factories.annotation;

import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.Model;
import org.brijframework.support.model.Models;
import org.brijframework.util.reflect.ReflectionUtils;


public class AnnotationModelMetaInfoFactory extends MetaFactoryImpl{
	
	private AnnotationModelMetaInfoFactory() {
	}
	private static AnnotationModelMetaInfoFactory factory;

	@Assignable
	public static AnnotationModelMetaInfoFactory getFactory() {
		if (factory == null) {
			factory = new AnnotationModelMetaInfoFactory();
			factory.loadFactory();
		}
		return factory;
	}

	@Override
	public AnnotationModelMetaInfoFactory loadFactory() {
		ReflectionUtils.getInternalClassList().forEach(target -> {
			if (target.isAnnotationPresent(Models.class) || target.isAnnotationPresent(Model.class)) {
				this.register(target);
			}
		});
		return this;
	}

	public void register(Class<?> target) {
		if (target.isAnnotationPresent(Models.class)) {
			Models models = target.getAnnotation(Models.class);
			for (Model metaSetup : models.value()) {
				this.register(target, metaSetup);
			}
		}
		if (target.isAnnotationPresent(Model.class)) {
			Model metaSetup = target.getAnnotation(Model.class);
			this.register(target, metaSetup);
		}
	}
}
