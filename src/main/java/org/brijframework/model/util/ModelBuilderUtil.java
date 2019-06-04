package org.brijframework.model.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.brijframework.meta.factories.asm.PropertyMetaFactoryImpl;
import org.brijframework.meta.impl.PropertyMeta;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.support.Constants;

public class ModelBuilderUtil {
	
	public static Class<?> getCurrentClass(Class<?> cls, String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Class<?> keyClass=cls;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			keyClass=getProperty(keyClass, key);
		}
		return keyClass;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentInstance(Object object,String _keyPath) {
		if(object instanceof Map) {
			return getCurrentFromMapped((Map<String, Object> )object,_keyPath);
		}else {
			return getCurrentFromInstance(object, _keyPath);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentInstance(Object object,Access access, String _keyPath) {
		if(object instanceof Map) {
			return getCurrentFromMapped((Map<String, Object> )object, access,_keyPath);
		}else {
			return getCurrentFromInstance(object,access, _keyPath);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCurrentFromInstance(Object object,Access access,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			current=getProperty(current, access,key);
		}
		return (T) current;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentFromInstance(Object object,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		StringBuffer point=new StringBuffer();
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			if(key.contains(Constants.OPEN_BRAKET)) {
				key=key.split("\\"+Constants.OPEN_BRAKET)[0];
			}
			Object temp=getProperty(current, Access.PRIVATE,key);
			if(temp==null) {
				temp=InstanceUtil.getInstance(getProperty(current.getClass(),key.toString()));
				setProperty(current, Access.PRIVATE, key, temp);
			}
			if(i < keyArray.length-2) {
				point.append(".");
			}
			current=temp;
		}
		if(_keyPath.endsWith(Constants.CLOSE_BRAKET)) {
			String key = _keyPath;
			if(_keyPath.contains(Constants.OPEN_BRAKET)) {
				key=_keyPath.split("\\"+Constants.OPEN_BRAKET)[0];
			}
			current=getProperty(current, Access.PRIVATE,key);
		}
		return (T) current;
	}

	@SuppressWarnings("unchecked")
	public static  <T> T getCurrentFromMapped(Map<String, Object> object,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			PropertyMeta property= PropertyMetaFactoryImpl.getFactory().getPropertyInfo(current.getClass().getSimpleName(),key);
			Access access = property!=null ?property.getAccess():Access.PUBLIC;
			current=getProperty(current,access,key);
		}
		return (T) current;
	}
	
	
	@SuppressWarnings("unchecked")
	public static  <T> T getCurrentFromMapped(Map<String, Object> object,Access access,String _keyPath) {
		String[] keyArray=_keyPath.split(Constants.SPLIT_DOT);
		Object current=object;
		for (int i = 0; i < keyArray.length-1; i++) {
			String key = keyArray[i];
			current=getProperty(current,access,key);
		}
		return (T) current;
	}
	
	public static  Class<?> getProperty(Class<?> _class, String _field) {
		Field field=FieldUtil.getField(_class, _field, Access.PRIVATE);
		if(field==null) {
			return null;
		}
		return field.getType();
	}
	

	@SuppressWarnings("unchecked")
	public static  <T> T getProperty(Object current,Access access, String key) {
		if(current instanceof Object[]) {
			return getPropertyArray((Object[]) current,access,key);
		}else if(current instanceof Collection) {
			return getPropertyCollection((Collection<?>) current,access,key);
		}else if(current instanceof Map) {
			return getPropertyMap((Map<String, Object>) current,key);
		}else {
			return getPropertyObject(current,access, key);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object current,Access access, String key,Object value) {
		if(current instanceof Object[]) {
			return setPropertyArray((Object[]) current,access,key,value);
		}else if(current instanceof Collection) {
			return setPropertyCollection((Collection<?>) current,access,key,value);
		}else if(current instanceof Map) {
			return setPropertyMap((Map<String, Object>) current,key,value);
		}else {
			return setPropertyObject(current,access, key,value);
		}
	}
	
	public static <T> T setProperty(Object current,int index,Access access, String key,Object value) {
		if(current instanceof Object[]) {
			Object objects[]=(Object[]) current;
			if(key==null) {
				objects[index]=value;
			}else {
				return setProperty(objects[index],access,key,value);
			}
		}else if(current instanceof Collection) {
			@SuppressWarnings("unchecked")
			Collection<Object> collection=(Collection<Object>) current;
			if(key==null) {
				if(collection.size()>index) {
				   Object objects[]=collection.toArray();
				   objects[index]=value;
				   collection.clear();
				   collection.addAll(Arrays.asList(objects));
				}else {
					collection.add(value);
				}
			}else {
				int inc=0;
				for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();) {
					if(inc==index) {
					Object object = (Object) iterator.next();
					  return setProperty(object,access,key,value);
					}
					inc++;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object current, String key,Access access,Object value) {
		if(current instanceof Object[]) {
			return setPropertyArray((Object[]) current,access, key,value);
		}else if(current instanceof Collection) {
			return setPropertyCollection((Collection<?>) current,access,key,value);
		}else if(current instanceof Map) {
			return setPropertyMap((Map<String, Object>) current,key,value);
		}else {
			return setPropertyObject(current, access, key, value);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T> T setPropertyArray(Object[] current, Access access, String key, Object value) {
		Collection<Object> objects=new ArrayList<>();
		for(Object object:current){
			objects.add(setProperty(object,access, key,value));
		};
		return (T) objects;
	}

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyCollection(Collection<?> current,Access access, String key, Object value) {
		Collection<Object> objects=new ArrayList<>();
		current.forEach(object->{
			objects.add(setProperty(object,access, key,value));
		});
		return (T) objects;
	}

	@SuppressWarnings("unchecked")
	private static <T> T setPropertyMap(Map<String, Object> current, String key, Object value) {
		Assertion.isTrue(!current.containsKey(key), AssertMessage.unbounded_key_message + " " + key);
		return (T) current.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyMap(Map<String, Object> current,String key ) {
		Assertion.isTrue(!current.containsKey(key), AssertMessage.unbounded_key_message + " " + key);
		return (T) current.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyCollection(Collection<?> collection,Access access,String key ) {
		Collection<Object> objects=new ArrayList<>();
		collection.forEach(object->{
			objects.add(getProperty(object,access, key));
		});
		return (T) objects;
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getPropertyArray(Object[] collection,Access access,String key ) {
		Collection<Object> objects=new ArrayList<>();
		for(Object object:collection){
			objects.add(getProperty(object,access, key));
		};
		return (T) objects.toArray();
	}
	
	public static  <T> T getPropertyObject(Object object,Access access,String key ) {
		return PropertyAccessorUtil.getProperty(object, key,access);
	}

	public static <T> T setPropertyObject(Object current,Access access, String keyPoint, Object _value) {
		return PropertyAccessorUtil.setProperty(current, keyPoint,access,_value);
	}
	
	

}
