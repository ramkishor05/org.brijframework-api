package org.brijframework.model.builder;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.meta.factories.asm.PropertyMetaFactoryImpl;
import org.brijframework.meta.impl.PropertyMeta;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.model.ModelBean;
import org.brijframework.model.factories.ModelFactory;
import org.brijframework.model.util.ModelBuilderUtil;
import org.brijframework.util.accessor.MetaAccessorUtil;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.meta.PointUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;
import org.brijframework.util.validator.ValidationUtil;

public class ModelBuilder implements ModelBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object instance;
	public ModelBuilder(Object instance) {
		this.instance=instance;
	}
	
	public static ModelBuilder getBuilder(Object instance) {
		return new ModelBuilder(instance);
	}
	
	public static ModelBuilder getBuilder(Class<?> cls) {
		return new ModelBuilder(InstanceUtil.getInstance(cls));
	}
	
	public static ModelBuilder getBuilder(String model) {
		return new ModelBuilder(ModelFactory.getFactory().getModel(model));
	}
	
	public Object getInstance() {
		return instance;
	}
	
	@Override
	public  <T> T setProperty(String _keyPath, T _value) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		Object keyInstance = this.getCurrentInstance(getInstance(), _keyPath);
		String currentPoint=PointUtil.currentPoint(_keyPath);
		Assertion.notNull(keyInstance, AssertMessage.root_object_null_message + " " + _keyPath);
		if(keyInstance instanceof Collection) {
			int index=Integer.valueOf(currentPoint.substring(currentPoint.indexOf(Constants.OPEN_BRAKET)+1, currentPoint.indexOf(Constants.CLOSE_BRAKET)));
			return setProperty(keyInstance, index, PointUtil.keyPoint(_keyPath), _value);
		}else {
			return setProperty(keyInstance, PointUtil.keyPoint(_keyPath), _value);
		}
	}

	@Override
	public  <T> T getProperty(String _keyPath) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		Object keyInstance = this.getCurrentInstance(getInstance(),_keyPath);
		Assertion.notNull(keyInstance, AssertMessage.root_object_null_message + " " + _keyPath);
		return getProperty(keyInstance, PointUtil.keyPoint(_keyPath));
	}

	@Override
	public  Boolean containsKey(String _keyPath) {
		Assertion.notNull(_keyPath, AssertMessage.arg_null_message);
		String _key = PointUtil.keyPoint(_keyPath);
		Class<?> current = this.getCurrentClass(getInstance().getClass(), _keyPath);
		return this.getProperty(current, _key) != null;
	}

	@Override
	public  Boolean containsValue(String _keyPath) {
		Assertion.notNull(_keyPath, AssertMessage.properties_null_message);
		if (!this.containsKey(_keyPath)) {
			return false;
		}
		return getProperty(_keyPath) != null;
	}

	@Override
	public  Class<?> typeOfProperty(String _keyPath) {
		Assertion.notNull(_keyPath, AssertMessage.arg_null_message);
		String _key = PointUtil.keyPoint(_keyPath);
		Class<?> current = this.getCurrentClass(getInstance().getClass(), _keyPath);
		Field field=MetaAccessorUtil.getFieldMeta(current, _key, Access.PRIVATE);
		if(field==null) {
			return null;
		}
		return field.getType();
	}
	
	@Override
	public Map<String, ?> setProperties(String[] _keys, Object... _values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> setProperties(String _keys, Object... _values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> safeProperties(String _keys, Object... _values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> setProperties(Map<String, Object> _properties) {
		Assertion.notNull(_properties, AssertMessage.arg_null_message);
		_properties.forEach((_keyPath,_value)->{
			setProperty(_keyPath, _value);
		});
		return _properties;
	}

	@Override
	public void fillProperties(Map<String, Object> _properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, ?> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> getProperties(String... _keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> safeProperties(String... _keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Boolean> cantainsProperties(String _keys) {
		// TODO Auto-generated method stub
		return null;
	}
   
	
	// for data
	protected <T> T setProperty(Object keyInstance,int index, String keyPoint, T _value) {
		return ModelBuilderUtil.setProperty(keyInstance,Access.PRIVATE, keyPoint, _value);
	}

	@SuppressWarnings("unchecked")
	protected <T> T setProperty(Object keyInstance, String keyPoint, T _value) {
		PropertyMeta propertyInfo =PropertyMetaFactoryImpl.getFactory().getPropertyInfo(keyInstance.getClass().getSimpleName(),keyPoint);
		Access key_access=propertyInfo!=null?propertyInfo.getAccess():Access.PUBLIC;
		ClassMeta metaInfo=MetaFactoryImpl.getFactory().getMeta(keyInstance.getClass().getSimpleName());
		Access md_access=metaInfo!=null?metaInfo.getAccess():Access.PUBLIC;
		Assertion.isTrue(!md_access.isAccess(key_access.getID()), AssertMessage.ILLEgGAL_ACCESS_MSG + "--->> "+keyInstance.getClass().getSimpleName()+"." + keyPoint);
		if(ValidationUtil.isDefault(_value) && propertyInfo!=null) {
			_value=(T) propertyInfo.getValue();
		}
		Assertion.isTrue(ValidationUtil.isDefault(_value) && propertyInfo!=null && propertyInfo.isRequired() , AssertMessage.field_value_null_message + "--->> "+keyInstance.getClass().getSimpleName()+"." + keyPoint);
		return ModelBuilderUtil.setProperty(keyInstance,Access.PRIVATE, keyPoint, _value);
	}

	protected <T> T getProperty(Object keyInstance, String keyPoint) {
		PropertyMeta propertyInfo =PropertyMetaFactoryImpl.getFactory().getPropertyInfo(keyInstance.getClass().getSimpleName(),keyPoint);
		Access key_access=propertyInfo!=null?propertyInfo.getAccess():Access.PUBLIC;
		ClassMeta metaInfo=MetaFactoryImpl.getFactory().getMeta(keyInstance.getClass().getSimpleName());
		Access md_access=metaInfo!=null?metaInfo.getAccess():Access.PUBLIC;
		Assertion.isTrue(!md_access.isAccess(key_access.getID()), AssertMessage.ILLEgGAL_ACCESS_MSG + "--->> "+keyInstance.getClass().getSimpleName()+"." + keyPoint);
		return ModelBuilderUtil.getProperty(keyInstance,Access.PRIVATE, keyPoint);
	}

	protected ClassMeta getClassInfo(Object keyInstance) {
		return MetaFactoryImpl.getFactory().getMeta(keyInstance.getClass().getSimpleName());
	}
	
	protected Object getCurrentInstance(Object instance, String _keyPath) {
		return ModelBuilderUtil.getCurrentFromInstance(instance, _keyPath);
	}
	
	protected Class<?> getCurrentClass(Class<? extends Object> cls, String _keyPath) {
		return ModelBuilderUtil.getCurrentClass(cls, _keyPath);
	}

	
}
