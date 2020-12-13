package ins.sino.claimcar.commom.vo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class Refs {

	public static Object invoke(Object obj,final String methodName,Object[] args) {
		if(obj==null){
			throw new NullPointerException("执行对象不能为null");
		}
		if(methodName==null){
			throw new NullPointerException("执行方法不能为null");
		}
		final Object[] ps = ( args==null ) ? new Object[0] : args;
		try{
			Class c = obj.getClass();
			Set ms = findMethod(c,new MethodFilter() {

				public boolean accept(Method method) {
					if(method.getName().equals(methodName)){
						Class[] parameterTypes = method.getParameterTypes();
						if(parameterTypes.length!=ps.length){
							return false;
						}
						for(int i = 0; i<parameterTypes.length; ++i){
							if(ps[i]==null){
								continue;
							}
							if(parameterTypes[i].isPrimitive()){
								parameterTypes[i] = Refs.getWrapper(parameterTypes[i]);
							}
							if( !( parameterTypes[i].isAssignableFrom(ps[i].getClass()) )){
								return false;
							}
						}
						return true;
					}
					return false;
				}
			});
			if(ms.size()==0){
				throw new IllegalArgumentException("类："+c+"无法通过方法名："+methodName+"和相关参数确定一个执行方法，请检查调用的参数是否有误");
			}
			Method m = (Method)ms.iterator().next();
			m.setAccessible(true);
			return m.invoke(obj,ps);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public static Set<Method> findMethod(Class c,MethodFilter methodFilter) {
		return findMethod(c,methodFilter,null);
	}

	private static Set<Method> findMethod(Class c,MethodFilter methodFilter,Set<Method> methods) {
		if(methods==null){
			methods = new LinkedHashSet();
		}
		Method[] ms = c.getDeclaredMethods();
		for(Method m:ms){
			if(methods.contains(m)){
				continue;
			}
			if(methodFilter==null){
				methods.add(m);
			}else if(methodFilter.accept(m)){
				methods.add(m);
			}
		}

		if( !( c.getSuperclass().equals(Object.class) )){
			findMethod(c.getSuperclass(),methodFilter,methods);
		}
		return methods;
	}

	public static Set<Field> findField(Class c,FieldFilter fieldFilter) {
		return findField(c,fieldFilter,null);
	}

	private static Set<Field> findField(Class c,FieldFilter fieldFilter,Set<Field> fields) {
		if(fields==null){
			fields = new LinkedHashSet();
		}
		Field[] fs = c.getDeclaredFields();
		for(Field f:fs){
			if(fields.contains(f)){
				continue;
			}
			if(fieldFilter==null){
				fields.add(f);
			}else if(fieldFilter.accept(f)){
				fields.add(f);
			}
		}

		if( !( c.getSuperclass().equals(Object.class) )){
			findField(c.getSuperclass(),fieldFilter,fields);
		}
		return fields;
	}

	private static Class getWrapper(Class c) {
		if(c.isPrimitive()){
			if(c.equals(Integer.TYPE)){
				c = Integer.class;
			}
			if(c.equals(Boolean.TYPE)){
				c = Boolean.class;
			}
			if(c.equals(Byte.TYPE)){
				c = Byte.class;
			}
			if(c.equals(Short.TYPE)){
				c = Short.class;
			}
			if(c.equals(Character.TYPE)){
				c = Character.class;
			}
			if(c.equals(Long.TYPE)){
				c = Long.class;
			}
			if(c.equals(Float.TYPE)){
				c = Float.class;
			}
			if(c.equals(Double.TYPE)){
				c = Double.class;
			}
			if(c.equals(Void.TYPE)){
				c = Void.class;
			}
		}
		return c;
	}

	public static Object get(Object obj,final String fieldName) {
		if(obj==null){
			throw new NullPointerException("执行对象不能为null");
		}
		if(fieldName==null){
			throw new NullPointerException("执行属性不能为null");
		}
		Class c = obj.getClass();
		Set fs = findField(c,new FieldFilter() {

			public boolean accept(Field method) {
				return ( method.getName().equals(fieldName) );
			}
		});
		if(fs.size()==0){
			throw new IllegalArgumentException("类："+c+"无法通过字段名："+fieldName+"找到相应的字段，请检查调用的参数是否有误");
		}
		Field f = (Field)fs.iterator().next();
		f.setAccessible(true);
		try{
			return f.get(obj);
		}catch(IllegalArgumentException e){
			throw new RuntimeException(e);
		}catch(IllegalAccessException e){
			throw new RuntimeException(e);
		}
	}

	public static void set(Object obj,final String fieldName,Object value) {
		if(obj==null){
			throw new NullPointerException("执行对象不能为null");
		}
		if(fieldName==null){
			throw new NullPointerException("执行属性不能为null");
		}
		Class c = obj.getClass();
		Set fs = findField(c,new FieldFilter() {
			public boolean accept(Field field) {
				return ( field.getName().equals(fieldName) );
			}
		});
		if(fs.size()==0){
			throw new IllegalArgumentException("类："+c+"无法通过字段名："+fieldName+"找到相应的字段，请检查调用的参数是否有误");
		}
		Field f = (Field)fs.iterator().next();
		f.setAccessible(true);
		try{
			f.set(obj,value);
		}catch(IllegalArgumentException e){
			throw new RuntimeException(e);
		}catch(IllegalAccessException e){
			throw new RuntimeException(e);
		}
	}

	public static abstract interface FieldFilter {

		public abstract boolean accept(Field paramField);
	}

	public static abstract interface MethodFilter {

		public abstract boolean accept(Method paramMethod);
	}
}
