package ee.pri.bcup.common.message.serialize;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonPrinter;

import ee.pri.bcup.common.model.pool.table.BallSpec;
import ee.pri.bcup.common.model.pool.table.PoolGameSpec;

public class SimpleSerializer {
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Class<T> clazz) {
		JsonParser parser = new JsonParser();
		JsonElement root = parser.parse(json);
		
		return (T) fromJsonElement(root, clazz);
	}
	
	@SuppressWarnings("unchecked")
	private static Object fromJsonElement(JsonElement element, Class<?> clazz) {
		if (element instanceof JsonNull) {
			return null;
		} if (element instanceof JsonPrimitive) {
			JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
			if (clazz == int.class) {
				return element.getAsInt();
			} if (clazz == boolean.class) {
				return element.getAsBoolean();
			} else if (clazz == long.class) {
				return element.getAsLong();
			} else if (clazz.isEnum()) {
				return Enum.valueOf((Class<Enum>) clazz, jsonPrimitive.getAsString());
			} else if (Long.class == clazz) {
				return element.getAsLong();
			} else if (Double.class == clazz) {
				return element.getAsDouble();
			} else if (Integer.class == clazz) {
				return element.getAsInt();
			} else if (String.class == clazz) {
				return element.getAsString();
			} else {
				throw new RuntimeException("Unknown primitive type " + clazz);
			}
		} else if (element instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject) element;
			Method[] methods = getMethods(clazz);
			
			Object object = null;
			try {
				object = clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Cannot deserialize " + clazz + " from " + element);
			}
			
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Method method = findSetter(methods, entry.getKey());
				
				if (method == null) {
					throw new RuntimeException("No setter for " + entry.getKey() + " in " + clazz);
				}
				
				Class<?> type = method.getParameterTypes()[0];
				if (type.isArray()) {
					JsonArray jsonArray = (JsonArray) entry.getValue();
					Class<?> elementType = type.getComponentType();
					Object array = Array.newInstance(elementType, jsonArray.size());
					for (int i = 0; i < jsonArray.size(); i++) {
						Array.set(array, i, fromJsonElement(jsonArray.get(i), elementType));
					}
					
					invokeSetter(method, object, array);
				} else {
					invokeSetter(method, object, fromJsonElement(entry.getValue(), type));
				}
			}
			
			return object;
		} else {
			throw new RuntimeException("Cannot deserialize " + clazz + " from " + element);
		}
	}
	
	private static void invokeSetter(Method method, Object base, Object value) {
		try {
			method.invoke(base, new Object[] {value});
		} catch (Exception e) {
			throw new RuntimeException("Cannot invoke setter " + method, e);
		}
	}
	
	private static Method findSetter(Method[] methods, String name) {
		for (Method method : methods) {
			if (method.getName().equals("set" + name)
				&& method.getParameterTypes().length == 1) {
				
				return method;
			}
		}
		
		return null;
	}
	
	public static String toJson(Object object) {
		StringBuilder builder = new StringBuilder();
		try {
			new JsonPrinter().format(toJsonElement(object), builder, true);
			return builder.toString();
		} catch (IOException e) {
			throw new RuntimeException("Cannot serialize " + object, e);
		}
	}
	
	public static JsonElement toJsonElement(Object object) {
		if (object == null) {
			return new JsonNull();
		}
		
		Class<?> clazz = object.getClass();
		if (object instanceof Boolean) {
			return new JsonPrimitive((Boolean) object);
		} else if (object instanceof Character) {
			return new JsonPrimitive((Character) object);
		} else if (object instanceof Number) {
			return new JsonPrimitive((Number) object);
		} else if (object instanceof String) {
			return new JsonPrimitive((String) object);
		}
		
		if (object instanceof Enum<?>) {
			Enum<?> enumObject = (Enum<?>) object;
			return new JsonPrimitive((String) enumObject.name());
		}
		
		if (clazz.isArray()) {
			int length = Array.getLength(object);
			JsonArray array = new JsonArray();
			for (int i = 0; i < length; i++) {
				array.add(toJsonElement(Array.get(object, i)));
			}
			return array;
		} else {
			JsonObject jsonObject = new JsonObject();
			Method[] methods = getMethods(clazz);
			
			for (Method method : methods) {
				if (method.getParameterTypes().length == 0
					&& method.getName().startsWith("get")
					&& !"getClass".equals(method.getName())
					&& !"getDeclaringClass".equals(method.getName())) {
					
					String fieldName = method.getName().substring(3);
					jsonObject.add(fieldName, invokeGetter(object, method));
				}
			}
			
			return jsonObject;
		}
	}
	
	private static Method[] getMethods(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		
		Arrays.sort(methods, new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		return methods;
	}
	
	private static JsonElement invokeGetter(Object object, Method method) {
		try {
			return toJsonElement(method.invoke(object));
		} catch (Exception e) {
			throw new RuntimeException("Cannot serialize object", e);
		}
	}

	public static void main(String[] args) {
		PoolGameSpec gameSpec = new PoolGameSpec();
		gameSpec.setBalls(new BallSpec[] {new BallSpec()});
		System.out.println(toJson(gameSpec));
		
		PoolGameSpec read = fromJson(toJson(gameSpec), PoolGameSpec.class);
		
		System.out.println(read);
	}
}
