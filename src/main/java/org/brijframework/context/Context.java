package org.brijframework.context;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.lifecycle.Initializer;

public interface Context extends Initializer {
	
	public void initialize(Context context) ;
	
	/**
	 * this is load and start root internal process of the context hierarchy.
	 * 
	 * @return void
	 */
	public void startup();

	/**
	 * this is release root internal process of the context hierarchy.
	 * 
	 * @return void
	 */
	public void destory();

	/**
	 * Return the parent context, or {@code null} if there is no parent and this
	 * is the root of the context hierarchy.
	 * 
	 * @return the parent context, or {@code null} if there is no parent
	 */
	public Context getParent();
	
	
	/**
	 * Return the Container map of context, or {@code null} if there is no parent and this
	 * is the root of the context hierarchy.
	 * 
	 * @return the parent context, or {@code null} if there is no parent
	 */
	public ConcurrentHashMap<Object, Container> getContainers();

	
}