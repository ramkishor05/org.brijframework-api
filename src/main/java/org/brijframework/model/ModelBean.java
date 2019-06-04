package org.brijframework.model;

import java.util.Map;

import org.brijframework.ObjectBean;
import org.brijframework.model.builder.ModelBuilder;
import org.brijframework.util.printer.GraphPrinter;

/***
 * 
 * @author Ram Kishor
 *
 */
public interface ModelBean extends ObjectBean {

	/***
	 * this provides us to set value of key for object.
	 * @param _key
	 * @param _value
	 * @return
	 */
	public default <T> T setProperty(String _keyPath, T _value) {
		return ModelBuilder.getBuilder(this).setProperty(_keyPath, _value);
	};

	/****
	 * this provides us to get value of key for object.
	 * @param _key
	 * @return
	 */
	public  default <T> T getProperty(String _keyPath) {
		return ModelBuilder.getBuilder(this).getProperty(_keyPath);
	}
	
	/***
	 * this provides us to check key which is contains or not for object.
	 * @param _key
	 * @return
	 */
	public default Boolean containsKey(String _keyPath) {
		return ModelBuilder.getBuilder(this).containsKey(_keyPath);
	}

	/****
	 * this provides us to check value of key which is contains or not for object.
	 * @param _key
	 * @return
	 */
	public default Boolean containsValue(String _keyPath) {
		return ModelBuilder.getBuilder(this).containsValue(_keyPath);
	}
	
	/***
	 * this provides us to get type of key for object.
	 * @param _key
	 * @return
	 */
	public default Class<?> typeOfProperty(String _keyPath){
		return ModelBuilder.getBuilder(this).typeOfProperty(_keyPath);
	}
	
	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> setProperties(String[] _keyPaths, Object... _values){
		return ModelBuilder.getBuilder(this).setProperties(_keyPaths,_values);
	}

	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> setProperties(String _keys, Object... _values){
		return ModelBuilder.getBuilder(this).setProperties(_keys,_values);
	}

	/***
	 * this provides us to set values of keys which is contains or not for object.
	 * 
	 * @param _keys
	 * @param _values
	 * @return
	 */
	public default Map<String, ?> safeProperties(String _keyPath, Object... _values){
		return ModelBuilder.getBuilder(this).safeProperties(_keyPath,_values);
	}

	/***
	 * this provides us to set values of keys for object.
	 * 
	 * @param _properties
	 * @return
	 */
	public default Map<String, ?> setProperties(Map<String, Object> _properties){
		return ModelBuilder.getBuilder(this).setProperties(_properties);
	}
	
	/***
	 * this provides us to fill values of keys for object.
	 * 
	 * @param _properties
	 * @return
	 */
	public default void fillProperties(Map<String, Object> _properties) {
	   ModelBuilder.getBuilder(this).fillProperties(_properties);
	}

	/***
	 * this provides us to get properties for object.
	 * @return
	 */
	public default Map<String, ?> getProperties(){
		return ModelBuilder.getBuilder(this).getProperties();
	}

	/***
	 * this provides us to get properties for object.
	 * @param _keys
	 * @return
	 */
	public default Map<String, ?> getProperties(String... _keyPath){
		return ModelBuilder.getBuilder(this).getProperties(_keyPath);
	}

	/***
	 * this provides us to get safe properties for object.
	 * @param _keys
	 * @return
	 */
	public default Map<String, ?> safeProperties(String... _keyPath){
		return ModelBuilder.getBuilder(this).safeProperties(_keyPath);
	}

	/***
	 * this provides us to check keys for object which is cantains or not.
	 * @param _keys
	 * @return
	 */
	public default Map<String, Boolean> cantainsProperties(String _keyPath){
		return ModelBuilder.getBuilder(this).cantainsProperties(_keyPath);
	}
	
	public default void printObject() {
		GraphPrinter.getPrinter(this).printToScreen();
	}

}
