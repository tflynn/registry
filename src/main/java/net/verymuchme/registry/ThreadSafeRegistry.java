package net.verymuchme.registry;

import java.util.HashMap;

import net.verymuchme.commonstatus.CommonStatus;

/**
 * <p>Provides a thread-safe location to store objects that need to be shared across threads.</p>
 * 
 * @author tracy
 *
 */
public class ThreadSafeRegistry {

	/**
	 * Internal Registry instance
	 */
	private static HashMap<String,Object> registry = new HashMap<String,Object>();
	
	/**
	 * Get a registered object - thread safe
	 * 
	 * @param registryName Name of registered object
	 * 
	 * @return Registered object or null if not present
	 */
	public static Object retrieve(String registryName) {
		synchronized(ThreadSafeRegistry.class) {
			return ThreadSafeRegistry.registry.get(registryName);
		}
	}

	/**
	 * <p>Get a registered object. Register the default if registered object does not exist. Thread-safe.</p>
	 * 
	 * <p>CommonStatus usage</p>
	 * <ul>
	 * <li>status - always true since object always returned</li>
	 * <li>Boolean property "defaultUsed" - indicates whether the default object was registered and returned</li>
	 * <li>Object property specified by registryName contains returned object</li>
	 * </ul>
	 * 
	 * @param registryName
	 * @param defaultObject
	 * @return CommonStatus
	 */
	public static CommonStatus retrieve(String registryName, Object defaultObject) {
		synchronized(ThreadSafeRegistry.class) {
			boolean defaultUsed = false;
			Object registeredObject = ThreadSafeRegistry.registry.get(registryName);
			if (registeredObject == null) {
				registeredObject = defaultObject;
				ThreadSafeRegistry.register(registryName, registeredObject);
				defaultUsed = false;
			}
			CommonStatus status = new CommonStatus();
			status.setStatus(true);
			status.setOjectProperty(registryName, registeredObject);
			status.setBooleanProperty("defaultUsed", defaultUsed);
			return status;
		}
	}
	
	/**
	 * Register an object - thread safe
	 * 
	 * @param registryName Name of registered object
	 * @param registeredObject Object to register
	 */
	public static void register(String registryName, Object registeredObject) {
		synchronized(ThreadSafeRegistry.class) {
			ThreadSafeRegistry.registry.put(registryName,registeredObject);
		}
	}
}
