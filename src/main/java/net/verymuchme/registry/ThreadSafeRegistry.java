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
	 * Get a registered object. Register the default if registered object does not exist. Thread-safe.
	 * 
	 * @param registryName
	 * @param defaultObject
	 * @return CommonStatus object - status = true if registered object found, false otherwise. Object returned in property specified by supplied registryName.
	 */
	public static CommonStatus retrieve(String registryName, Object defaultObject) {
		synchronized(ThreadSafeRegistry.class) {
			boolean registeredObjectFound = true;
			Object registeredObject = ThreadSafeRegistry.registry.get(registryName);
			if (registeredObject == null) {
				registeredObject = defaultObject;
				ThreadSafeRegistry.register(registryName, registeredObject);
				registeredObjectFound = false;
			}
			CommonStatus status = new CommonStatus();
			status.setStatus(registeredObjectFound);
			status.setOjectProperty(registryName, registeredObject);
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
