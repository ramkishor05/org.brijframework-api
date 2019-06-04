package org.brijframework.model.factories.json;

import java.lang.reflect.AccessibleObject;
import java.util.Map.Entry;

import org.brijframework.meta.KeyInfo;
import org.brijframework.meta.factories.asm.PropertyMetaFactoryImpl;
import org.brijframework.meta.impl.PropertyMeta;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.support.model.Model;
import org.brijframework.support.model.Models;
import org.brijframework.support.model.Property;
import org.brijframework.support.model.Relation;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;

@DepandOn(depand = JsonModelMetaInfoFactory.class)
public class JsonPropertyMetaInfoFactory extends PropertyMetaFactoryImpl {
	private static JsonPropertyMetaInfoFactory factory;

	@Assignable
	public static JsonPropertyMetaInfoFactory getFactory() {
		if (factory == null) {
			factory = new JsonPropertyMetaInfoFactory();
			factory.loadFactory();
		}
		return factory;
	}
	
	public JsonPropertyMetaInfoFactory loadFactory() {
		return this;
	}
	
	public void register(Class<?> target) {
		if (target.isAnnotationPresent(Models.class)) {
			Models models = target.getAnnotation(Models.class);
			for (Model metaSetup : models.value()) {
				String model=Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id();
				this.register(target,model);
			}
		}else if (target.isAnnotationPresent(Model.class)) {
			Model metaSetup = target.getAnnotation(Model.class);
			String model=Constants.DEFAULT.equals(metaSetup.id())?target.getSimpleName():metaSetup.id();
			this.register(target,model);
		}else {
			this.register(target,target.getSimpleName());
		}
	}

	private void register(Class<?> cls,String model) {
		FieldUtil.getAllField(cls, Access.PRIVATE).forEach(target -> {
			if (target.isAnnotationPresent(Property.class) || target.isAnnotationPresent(Relation.class)) {
				this.register(model, target);
			}
		});
	}

	public PropertyMeta getPropertyInfo(String id) {
		for(Entry<KeyInfo, PropertyMeta> entry:getCache().entrySet()) {
			if(entry.getKey().getId().equals(id)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void register(String model, AccessibleObject target) {
		if (target.isAnnotationPresent(Property.class)) {
			Property property = target.getAnnotation(Property.class);
			this.register(model, target, property);
		}
		if (target.isAnnotationPresent(Relation.class)) {
			Relation property = target.getAnnotation(Relation.class);
			this.register(model, target, property);
		}
	}
}
