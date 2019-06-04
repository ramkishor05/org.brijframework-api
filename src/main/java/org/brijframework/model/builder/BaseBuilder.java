package org.brijframework.model.builder;

import java.lang.reflect.Field;
import java.util.Map;

import org.brijframework.ObjectBean;
import org.brijframework.meta.factories.asm.MetaFactoryImpl;
import org.brijframework.meta.factories.asm.PropertyMetaFactoryImpl;
import org.brijframework.meta.impl.PropertyMeta;
import org.brijframework.meta.reflect.ClassMeta;
import org.brijframework.model.util.ModelBuilderUtil;
import org.brijframework.util.accessor.MetaAccessorUtil;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.meta.PointUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;
import org.brijframework.util.validator.ValidationUtil;

public class BaseBuilder implements ObjectBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object instance;
	
	public BaseBuilder(Object instance) {
		this.instance=instance;
	}

	public static BaseBuilder getBuilder(Object instance) {
		return new BaseBuilder(instance);
	}
	
	@Override
	public  <T> T setProperty(String _keyPath, T _value) {
		Assertion.notEmpty(_keyPath, "Key should not be null or empty");
		Object keyInstance = this.getCurrentInstance(getInstance(), _keyPath);
		Assertion.notNull(keyInstance, AssertMessage.root_object_null_message + " " + _keyPath);
		return setProperty(keyInstance, PointUtil.keyPoint(_keyPath), _value);
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
	
	@SuppressWarnings("unchecked")
	protected <T> T setProperty(Object keyInstance, String keyPoint, T _value) {
		if(keyPoint.endsWith(Constants.CLOSE_BRAKET)) {
			String key = keyPoint;
			if(keyPoint.contains(Constants.OPEN_BRAKET)) {
				key=keyPoint.split("\\"+Constants.OPEN_BRAKET)[0];
			}
			int index=Integer.valueOf(keyPoint.substring(keyPoint.indexOf(Constants.OPEN_BRAKET)+1, keyPoint.indexOf(Constants.CLOSE_BRAKET)));
			System.out.println("index="+index);
			PropertyMeta propertyInfo =PropertyMetaFactoryImpl.getFactory().getPropertyInfo(keyInstance.getClass().getSimpleName(),key);
			Access key_access=propertyInfo!=null?propertyInfo.getAccess():Access.PUBLIC;
			ClassMeta metaInfo=MetaFactoryImpl.getFactory().getMeta(keyInstance.getClass().getSimpleName());
			Access md_access=metaInfo!=null?metaInfo.getAccess():Access.PUBLIC;
			Assertion.isTrue(!md_access.isAccess(key_access.getID()), AssertMessage.ILLEgGAL_ACCESS_MSG + "--->> "+keyInstance.getClass().getSimpleName()+"." + key);
			if(ValidationUtil.isDefault(_value) && propertyInfo!=null) {
				_value=(T) propertyInfo.getValue();
			}
			Assertion.isTrue(ValidationUtil.isDefault(_value) && propertyInfo!=null && propertyInfo.isRequired() , AssertMessage.field_value_null_message + "--->> "+keyInstance.getClass().getSimpleName()+"." + keyPoint);
			return ModelBuilderUtil.setProperty(keyInstance,index,Access.PRIVATE,null, _value);
		}else {
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
		
	}

	protected <T> T getProperty(Object keyInstance, String keyPoint) {
		PropertyMeta propertyInfo =PropertyMetaFactoryImpl.getFactory().getPropertyInfo(keyInstance.getClass().getSimpleName(),keyPoint);
		Access key_access=propertyInfo!=null?propertyInfo.getAccess():Access.PUBLIC;
		ClassMeta metaInfo=MetaFactoryImpl.getFactory().getMeta(keyInstance.getClass().getSimpleName());
		Access md_access=metaInfo!=null?metaInfo.getAccess():Access.PUBLIC;
		Assertion.isTrue(!md_access.isAccess(key_access.getID()), AssertMessage.ILLEgGAL_ACCESS_MSG + "--->> "+keyInstance.getClass().getSimpleName()+"." + keyPoint);
		
		return ModelBuilderUtil.getProperty(keyInstance,Access.PRIVATE, keyPoint);
	}
	
	protected <T> T setProperty(Object keyInstance,int index, String keyPoint, T _value) {
		return ModelBuilderUtil.setProperty(keyInstance,Access.PRIVATE, keyPoint, _value);
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
	
	public Object getInstance() {
		return instance;
	}

	public Object callLogic(String name, Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,Object> toMap(){
		return PropertyAccessorUtil.getProperties(getInstance());
	}

}
