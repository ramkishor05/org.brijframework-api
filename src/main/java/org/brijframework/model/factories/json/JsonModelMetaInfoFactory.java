package org.brijframework.model.factories.json;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.meta.setup.ClassMetaSetup;
import org.brijframework.model.setup.ModelMetaSetup;
import org.brijframework.resources.factory.json.JsonResourceFactory;
import org.brijframework.resources.files.json.JsonResource;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;
import org.json.JSONException;


public class JsonModelMetaInfoFactory extends MetaFactoryImpl{
	
	public JsonModelMetaInfoFactory() {
	}
	private static JsonModelMetaInfoFactory factory;

	@Assignable
	public static JsonModelMetaInfoFactory getFactory() {
		if (factory == null) {
			factory = new JsonModelMetaInfoFactory();
			factory.loadFactory();
		}
		return factory;
	}

	@Override
	public JsonModelMetaInfoFactory loadFactory() {
		Collection<JsonResource> resources=JsonResourceFactory.factory().getResources("classpath:/model");
		for(JsonResource resource:resources) {
			if(resource.isJsonList()) {
			   try {
				  register(resource.toObjectList());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if(resource.isJsonObject()) {
				try {
					register(resource.toObjectMap());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public void register(List<Object> resources) {
		Assertion.notNull(resources, "Invalid resources");
		for(Object object:resources) {
			if(object instanceof Map) {
				register((Map<String, Object>)object);
			}
		}
	}

	public void register(Map<String, Object> resourceMap) {
		Class<?> target=org.brijframework.util.reflect.ClassUtil.getClass((String)resourceMap.get("type"));
		Assertion.notNull(target, "Invalid target :"+resourceMap.get("type"));
		ClassMetaSetup metaSetup=InstanceUtil.getInstance(ModelMetaSetup.class, resourceMap);
		this.register(target, metaSetup);
	}
}
