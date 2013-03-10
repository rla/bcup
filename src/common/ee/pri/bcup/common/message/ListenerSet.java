package ee.pri.bcup.common.message;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Very general helper class for distributing messages.
 * 
 * @author Raivo Laanemets
 */
public class ListenerSet {

	private Map<Class<?>, List<MethodMessageListener>> listeners = new HashMap<Class<?>, List<MethodMessageListener>>();
	
	/**
	 * Adds new message listener.
	 */
	public void addListener(Class<?> clazz, MethodMessageListener listener) {
		if (listeners.containsKey(clazz)) {
			listeners.get(clazz).add(listener);
		} else {
			List<MethodMessageListener> listeners = new ArrayList<MethodMessageListener>();
			listeners.add(listener);
			this.listeners.put(clazz, listeners);
		}
	}
	
	public void message(Message message) {
		final List<MethodMessageListener> listeners = this.listeners.get(message.getClass());
		if (listeners != null) {
			for (MethodMessageListener listener : listeners) {
				listener.message(message);
			}
		}
	}
	
	/**
	 * Helper method for processing message listeners in method form
	 * to receive messages.
	 * 
	 * @param object target of messages.
	 * @param scope scope of listeners.
	 */
	public void processListeners(Object object, ListenerScope scope) {
		Class<?> clazz = object.getClass();
		Method[] methods = clazz.getMethods();
		
		for (Method method : methods) {
			if (method.isAnnotationPresent(MessageReceiver.class)) {
				MessageReceiver receiver = method.getAnnotation(MessageReceiver.class);
				Class<? extends Message> messageClazz = receiver.value();
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length != 1) {
					throw new RuntimeException("Class " + clazz + "contains message" +
							"receiver with wrong number of arguments");
				}
				Class<?> receivableClazz = parameters[0];
				if (!receivableClazz.equals(messageClazz)) {
					throw new RuntimeException("Class " + clazz + "contains message" +
							"receiver with wrong parameters");
				}
				addListener(messageClazz, new MethodMessageListener(method, object, scope));
			}
		}
	}
	
	/**
	 * Clears listeners of the given scope.
	 */
	public void clearScope(ListenerScope scope) {
		Map<Class<?>, List<MethodMessageListener>> newMap = new HashMap<Class<?>, List<MethodMessageListener>>();
		
		for (Entry<Class<?>, List<MethodMessageListener>> entry : listeners.entrySet()) {
			newMap.put(entry.getKey(), clearScopeForClass(entry.getValue(), scope));
		}
		
		listeners = newMap;
	}
	
	private List<MethodMessageListener> clearScopeForClass(List<MethodMessageListener> list, ListenerScope scope) {
		ArrayList<MethodMessageListener> ret = new ArrayList<MethodMessageListener>();
		
		for (MethodMessageListener listener : list) {
			if (listener.getScope() != scope) {
				ret.add(listener);
			}
		}
		
		return ret;
	}
}
