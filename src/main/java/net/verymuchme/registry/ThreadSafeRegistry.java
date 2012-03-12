/* Copyright 2009-2012 Tracy Flynn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.verymuchme.registry;

import java.util.HashMap;

import net.verymuchme.commonreturn.CommonReturn;
import net.verymuchme.commonreturn.Status;

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
	 * <p>CommonReturn usage</p>
	 * <ul>
	 * <li>status - always true since object always returned</li>
	 * <li>Boolean property "defaultUsed" - indicates whether the default object was registered and returned</li>
	 * <li>Object property specified by registryName contains returned object</li>
	 * </ul>
	 * 
	 * @param registryName
	 * @param defaultObject
	 * @return CommonReturn
	 */
	public static CommonReturn<Object>  retrieve(String registryName, Object defaultObject) {
		synchronized(ThreadSafeRegistry.class) {
			Status status = Status.SUCCESS;
			boolean defaultUsed = false;
			Object registeredObject = ThreadSafeRegistry.registry.get(registryName);
			if (registeredObject == null) {
				registeredObject = defaultObject;
				ThreadSafeRegistry.register(registryName, registeredObject);
				defaultUsed = false;
			}
			CommonReturn<Object> commonReturn = new CommonReturn<Object>(registeredObject,null,status);
			
			//status.setStatus(true);
			//status.setOjectProperty(registryName, registeredObject);
			//status.setBooleanProperty("defaultUsed", new Boolean(defaultUsed));
			return commonReturn;
		}
	}

	/**
	 * <p>Get a registered object. Only create and register the default if registered object does not exist. Thread-safe.</p>
	 * 
	 * <p>CommonReturn usage</p>
	 * <ul>
	 * <li>status - true if object returned. false if error occurs during default object instantiation. If false, exception value set.</li>
	 * <li>Boolean property "defaultUsed" - indicates whether the default object was registered and returned</li>
	 * <li>Object property specified by registryName contains returned object</li>
	 * </ul>
	 * 
	 * @param registryName
	 * @param defaultObjectClassName Class name for default object
	 * @return CommonReturn
	 */
	public static CommonReturn retrieve(String registryName, String defaultObjectClassName) {
		synchronized(ThreadSafeRegistry.class) {
			CommonReturn commonReturn = new CommonReturn();
			boolean returnedStatus = true;
			boolean defaultUsed = false;
			Object registeredObject = ThreadSafeRegistry.registry.get(registryName);
			if (registeredObject == null) {
				try {
					registeredObject = Class.forName(defaultObjectClassName);
					ThreadSafeRegistry.register(registryName, registeredObject);
					defaultUsed = true;
				} catch (ClassNotFoundException e) {
					commonReturn.setException(e);
					registeredObject = null;
					returnedStatus = false;
					defaultUsed = false;
				}
			}
			commonReturn.setStatus(returnedStatus);
			commonReturn.setOjectProperty(registryName, registeredObject);
			commonReturn.setBooleanProperty("defaultUsed", new Boolean(defaultUsed));
			return commonReturn;
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
