package ee.pri.bcup.common.message;

import java.lang.reflect.Method;

/**
 * Game message listener that invokes the given method.
 * 
 * @author Raivo Laanemets
 */
// FIXME logging
public class MethodMessageListener {
	private Method method;
	private Object instance;
	private ListenerScope scope;

	public MethodMessageListener(Method method, Object instance, ListenerScope scope) {
		this.method = method;
		this.instance = instance;
		this.scope = scope;
	}

	public void message(Object message) {
		try {
			method.invoke(instance, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListenerScope getScope() {
		return scope;
	}

}
