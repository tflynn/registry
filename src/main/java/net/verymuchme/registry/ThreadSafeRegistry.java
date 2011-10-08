package net.verymuchme.registry;

import java.util.HashMap;

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
